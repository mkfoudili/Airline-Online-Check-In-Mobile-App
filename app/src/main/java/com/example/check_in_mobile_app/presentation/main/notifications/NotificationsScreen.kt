package com.example.check_in_mobile_app.presentation.main.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.components.notifications.NotificationCard
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.ui.theme.ErrorRed
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.check_in_mobile_app.ui.theme.Poppins
import com.example.domain.model.NotificationType

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = hiltViewModel(),
    onTabSelected: (TabItem) -> Unit = {},
    onNavigateToBooking: (String) -> Unit = {},
    onNavigateToCheckIn: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    // Handle routing events (e.g. from push notifications)
    LaunchedEffect(uiState.routingEvent) {
        uiState.routingEvent?.let { event ->
            when (event) {
                is RoutingEvent.NavigateToBooking -> onNavigateToBooking(event.bookingId)
                is RoutingEvent.NavigateToCheckIn -> onNavigateToCheckIn()
                is RoutingEvent.NavigateToNotifications -> { /* Already here */ }
            }
            viewModel.onRoutingEventHandled()
        }
    }

    NotificationsContent(
        uiState = uiState,
        isRefreshing = isRefreshing,
        onRefresh = { viewModel.refresh() },
        onMarkAllRead = { viewModel.markAllAsRead() },
        onNotificationClick = { notification ->
            viewModel.markSingleAsRead(notification.id)
        },
        onTabSelected = onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationsUiState,
    hasUnread: Boolean = false,
    isRefreshing: Boolean = false,
    onRefresh: () -> Unit = {},
    onMarkAllRead: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth().padding(end = 16.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.notification_title),
                                fontFamily = Poppins,
                                fontSize = 25.sp,
                                color = LocalAppColors.current.textPrimary,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = (-0.5).sp
                            )

                            if (uiState.isOffline) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(id = R.drawable.wifi_off2),
                                        contentDescription = null,
                                        tint = ErrorRed,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Text(
                                        text = stringResource(R.string.offline_badge),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = ErrorRed
                                    )
                                }
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
            }
        },
        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.NOTIFICATIONS,
                hasUnreadNotifications = hasUnread,
                onTabSelected = onTabSelected
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = LocalAppColors.current.textAccent)
                }
            } else if (uiState.errorMessage != null && !uiState.isOffline) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = uiState.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = onRefresh,
                    modifier = Modifier.fillMaxSize()
                ) {
                    if (uiState.isOffline) {
                        NotificationsOfflineContent()
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item { Spacer(modifier = Modifier.height(4.dp)) }

                            val groups = listOf(
                                "Today" to R.string.notification_group_today,
                                "Yesterday" to R.string.notification_group_yesterday,
                                "This Week" to R.string.notification_group_this_week,
                                "Earlier" to R.string.notification_group_earlier
                            )

                            val firstVisibleGroupKey = groups.find {
                                !uiState.groupedNotifications[it.first].isNullOrEmpty()
                            }?.first
                            val hasNotifications = firstVisibleGroupKey != null

                            groups.forEach { (groupKey, groupResId) ->
                                val notifications = uiState.groupedNotifications[groupKey]
                                if (!notifications.isNullOrEmpty()) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(horizontal = 16.dp, vertical = 8.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = stringResource(groupResId).uppercase(),
                                                style = MaterialTheme.typography.labelLarge.copy(
                                                    fontWeight = FontWeight.Bold,
                                                    color = LocalAppColors.current.textSecondary,
                                                    letterSpacing = 1.sp
                                                )
                                            )

                                            if (groupKey == firstVisibleGroupKey && !uiState.isOffline) {
                                                TextButton(
                                                    onClick = onMarkAllRead,
                                                    contentPadding = PaddingValues(0.dp),
                                                    modifier = Modifier.heightIn(min = 1.dp)
                                                ) {
                                                    Text(
                                                        text = stringResource(R.string.notification_mark_all_read).uppercase(),
                                                        style = MaterialTheme.typography.labelLarge.copy(
                                                            fontWeight = FontWeight.Bold,
                                                            color = LocalAppColors.current.textSecondary,
                                                            letterSpacing = 1.sp
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }

                                    items(notifications) { notification ->
                                        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                                            NotificationCard(
                                                notification = notification,
                                                onClick = { onNotificationClick(notification) }
                                            )
                                        }
                                    }
                                }
                            }

                            if (!hasNotifications) {
                                item {
                                    Box(
                                        modifier = Modifier.fillParentMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = stringResource(R.string.notification_empty_state),
                                            color = LocalAppColors.current.textSecondary
                                        )
                                    }
                                }
                            }

                            item { Spacer(modifier = Modifier.height(16.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationsOfflineContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wifi_off),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Text(
            text = stringResource(R.string.connection_timed_out),
            fontFamily = Poppins,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = LocalAppColors.current.textPrimary,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(R.string.connection_error_description),
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = LocalAppColors.current.textSecondary,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotificationsScreenPreview() {
    CheckInMobileAppTheme {
        NotificationsContent(
            uiState = NotificationsUiState(
                groupedNotifications = mapOf(
                    "Today" to listOf(
                        NotificationItem(
                            id = "1",
                            title = "Boarding Starts in 30m",
                            description = "Prepare your boarding pass and ID.",
                            timeAgo = "45m ago",
                            isRead = false,
                            type = NotificationType.BOARDING_REMINDER,
                            createdAt = System.currentTimeMillis()
                        )
                    )
                )
            ),
            onMarkAllRead = {}
        )
    }
}
