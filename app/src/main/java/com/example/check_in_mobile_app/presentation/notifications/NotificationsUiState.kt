package com.example.check_in_mobile_app.presentation.notifications

data class NotificationsUiState(
    val isLoading: Boolean = false,
    val groupedNotifications: Map<String, List<NotificationItem>> = emptyMap(),
    val errorMessage: String? = null
)

data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val flightCode: String? = null,
    val timeAgo: String,
    val isRead: Boolean,
    val type: NotificationType,
    val createdAt: Long = 0L
)

enum class NotificationType {
    BOARDING,
    CHECK_IN,
    DOCUMENT
}
