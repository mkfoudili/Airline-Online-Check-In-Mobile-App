package com.example.check_in_mobile_app.presentation.main.notifications

import com.example.domain.model.NotificationType

/**
 * UI State for the Notifications screen.
 */
data class NotificationsUiState(
    val isLoading: Boolean = false,
    val groupedNotifications: Map<String, List<NotificationItem>> = emptyMap(),
    val errorMessage: String? = null,
    val isOffline: Boolean = false,
    val routingEvent: RoutingEvent? = null
)

/**
 * UI model for a single notification item, adapted for the display.
 */
data class NotificationItem(
    val id: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val isRead: Boolean,
    val type: NotificationType,
    val createdAt: Long,
    val flightCode: String? = null,
    val bookingId: String? = null
)

/**
 * Navigation events triggered by push notification data.
 */
sealed class RoutingEvent {
    data class NavigateToBooking(val bookingId: String) : RoutingEvent()
    object NavigateToCheckIn : RoutingEvent()
    object NavigateToNotifications : RoutingEvent()
}
