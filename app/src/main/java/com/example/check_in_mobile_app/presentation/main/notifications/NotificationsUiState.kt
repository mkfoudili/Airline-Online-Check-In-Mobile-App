package com.example.check_in_mobile_app.presentation.main.notifications

import com.example.domain.model.NotificationType

/**
 * UI State for the Notifications screen.
 */
data class NotificationsUiState(
    val isLoading: Boolean = false,
    val groupedNotifications: Map<String, List<NotificationItem>> = emptyMap(),
    val errorMessage: String? = null,
    val routingEvent: RoutingEvent? = null
)

/**
 * UI model for a single notification item.
 */
data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val isRead: Boolean,
    val type: NotificationType,
    val createdAt: Long
)

/**
 * Represents a navigation event triggered by a push notification.
 */
sealed class RoutingEvent {
    data class NavigateToBooking(val bookingId: String) : RoutingEvent()
    object NavigateToCheckIn : RoutingEvent()
    object NavigateToNotifications : RoutingEvent()
}
