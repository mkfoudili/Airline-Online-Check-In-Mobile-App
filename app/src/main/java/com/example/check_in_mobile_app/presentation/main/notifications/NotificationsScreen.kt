package com.example.check_in_mobile_app.presentation.main.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.components.notifications.NotificationCard
import com.example.check_in_mobile_app.presentation.notifications.NotificationsViewModel
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Slate500
import com.example.check_in_mobile_app.ui.theme.SurfaceGray

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel = viewModel(),
    onTabSelected: (TabItem) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    NotificationsContent(
        uiState = uiState,
        onMarkAllRead = { viewModel.markAllAsRead() },
        onTabSelected = onTabSelected
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationsUiState,
    onMarkAllRead: () -> Unit,
    onTabSelected: (TabItem) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.notification_title),
                        style = MaterialTheme.typography.headlineSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.NOTIFICATIONS,
                onTabSelected = onTabSelected
            )
        },
        containerColor = SurfaceGray
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
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
                                            color = Slate500,
                                            letterSpacing = 1.sp
                                        )
                                    )
                                    if (groupKey == "Today") {
                                        TextButton(onClick = onMarkAllRead) {
                                            Text(
                                                text = stringResource(R.string.notification_mark_all_read),
                                                style = MaterialTheme.typography.labelMedium.copy(
                                                    color = Slate500
                                                )
                                            )
                                        }
                                    }
                                }
                            }

                            items(notifications) { notification ->
                                NotificationCard(
                                    notification = notification,
                                    onClick = { /* Handle notification click */ }
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
                                    color = Slate500
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
                            description = "Prepare your boarding pass and ID. Boarding for Group 1 will start soon.",
                            flightCode = "AA241",
                            timeAgo = "45m ago",
                            isRead = false,
                            type = NotificationType.BOARDING
                        )
                    ),
                    "Yesterday" to listOf(
                        NotificationItem(
                            id = "3",
                            title = "Check-in Confirmed",
                            description = "Check-in successful! Your seat 14C is confirmed. View your boarding pass.",
                            flightCode = "AA241",
                            timeAgo = "1d ago",
                            isRead = true,
                            type = NotificationType.CHECK_IN
                        )
                    )
                )
            ),
            onMarkAllRead = {}
        )
    }
}
