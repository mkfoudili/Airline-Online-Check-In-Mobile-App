package com.example.check_in_mobile_app.presentation.notifications

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.presentation.components.notifications.NotificationCard
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Slate500
import com.example.check_in_mobile_app.ui.theme.SurfaceGray

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel
) {
    val uiState by viewModel.uiState.collectAsState()
    NotificationsContent(
        uiState = uiState,
        onMarkAllRead = { viewModel.markAllAsRead() }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationsContent(
    uiState: NotificationsUiState,
    onMarkAllRead: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Notifications",
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
                    if (uiState.notifications.isNotEmpty()) {
                        item {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "TODAY",
                                    style = MaterialTheme.typography.labelLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Slate500,
                                        letterSpacing = 1.sp
                                    )
                                )
                                TextButton(onClick = onMarkAllRead) {
                                    Text(
                                        text = "Mark all read",
                                        style = MaterialTheme.typography.labelMedium.copy(
                                            color = Slate500
                                        )
                                    )
                                }
                            }
                        }
                    }

                    items(uiState.notifications) { notification ->
                        NotificationCard(
                            notification = notification,
                            onClick = { /* Handle notification click */ }
                        )
                    }

                    if (uiState.notifications.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = "No notifications", color = Slate500)
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
                notifications = listOf(
                    NotificationItem(
                        id = "1",
                        title = "Boarding Starts in 30m",
                        description = "Prepare your boarding pass and ID. Boarding for Group 1 will start soon.",
                        flightCode = "AA241",
                        timeAgo = "45m ago",
                        isRead = false,
                        type = NotificationType.BOARDING
                    ),
                    NotificationItem(
                        id = "2",
                        title = "Boarding Starts in 2hrs",
                        description = "Prepare your boarding pass and ID. Boarding for Group 1 will start soon.",
                        flightCode = "AA241",
                        timeAgo = "2h ago",
                        isRead = false,
                        type = NotificationType.BOARDING
                    ),
                    NotificationItem(
                        id = "3",
                        title = "Check-in Confirmed",
                        description = "Check-in successful! Your seat 14C is confirmed. View your boarding pass.",
                        flightCode = "AA241",
                        timeAgo = "2h ago",
                        isRead = true,
                        type = NotificationType.CHECK_IN
                    ),
                    NotificationItem(
                        id = "4",
                        title = "Travel Document Verified",
                        description = "Your passport scan has been successfully processed and verified.",
                        flightCode = null,
                        timeAgo = "5h ago",
                        isRead = true,
                        type = NotificationType.DOCUMENT
                    )
                )
            ),
            onMarkAllRead = {}
        )
    }
}
