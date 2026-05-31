package com.example.check_in_mobile_app.presentation.main.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.components.notifications.NotificationCard
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
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
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val hasUnread by viewModel.hasUnread.collectAsStateWithLifecycle()

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
        hasUnread = hasUnread,
        onMarkAllRead = { viewModel.markAllAsRead() },
        onNotificationClick = { notification ->
            viewModel.markSingleAsRead(notification.id)
            // Handle navigation on click if needed
        },
        onTabSelected = onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationsUiState,
    hasUnread: Boolean = false,
    onMarkAllRead: () -> Unit,
    onNotificationClick: (NotificationItem) -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(R.string.notification_title),
                            fontFamily = Poppins,
                            fontSize = 25.sp,
                            color = LocalAppColors.current.textPrimary,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = (-0.5).sp
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                    thickness = 1.dp
                )
                Spacer(modifier = Modifier.height(16.dp))
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
            } else if (uiState.errorMessage != null) {
                Text(
                    text = uiState.errorMessage,
                    modifier = Modifier.align(Alignment.Center),
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val groups = listOf(
                        "Today" to R.string.notification_group_today,
                        "Yesterday" to R.string.notification_group_yesterday,
                        "This Week" to R.string.notification_group_this_week,
                        "Earlier" to R.string.notification_group_earlier
                    )
                    var hasNotifications = false

                    groups.forEach { (groupKey, groupResId) ->
                        val notifications = uiState.groupedNotifications[groupKey]
                        if (!notifications.isNullOrEmpty()) {
                            hasNotifications = true
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp, bottom = 8.dp),
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
                                    if (groupKey == "Today") {
                                        TextButton(onClick = onMarkAllRead) {
                                            Text(
                                                text = stringResource(R.string.notification_mark_all_read),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = LocalAppColors.current.textSecondary
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            items(notifications) { notification ->
                                NotificationCard(
                                    notification = notification,
                                    onClick = { onNotificationClick(notification) }
                                )
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
                }
            }
        }
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
