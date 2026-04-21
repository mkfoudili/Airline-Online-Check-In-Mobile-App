package com.example.check_in_mobile_app.presentation.main.notifications

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationItem> = emptyList(),
    val errorMessage: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val flightCode: String? = null,
    val timeAgo: String,
    val isRead: Boolean,
    val type: NotificationType
)

enum class NotificationType {
    BOARDING,
    CHECK_IN,
    DOCUMENT
}
