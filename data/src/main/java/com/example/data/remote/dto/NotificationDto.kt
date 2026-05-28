package com.example.data.remote.dto

/**
 * Data Transfer Object for Notification.
 */
data class NotificationDto(
    val notificationId: String,
    val type: String,
    val title: String,
    val body: String,
    val screen: String,
    val bookingId: String?,
    val boardingPassId: String?,
    val passengerId: String?,
    val flightId: String?,
    val isRead: Boolean,
    val createdAt: String
)

/**
 * Response wrapper for a list of notifications.
 */
data class NotificationListResponse(
    val success: Boolean,
    val data: List<NotificationDto>,
    val message: String?
)

/**
 * Response wrapper for a single notification.
 */
data class NotificationResponse(
    val success: Boolean,
    val data: NotificationDto,
    val message: String?
)

/**
 * Request body for registering a push token.
 */
data class RegisterTokenRequest(
    val token: String
)

/**
 * Response body for read-all notifications.
 */
data class ReadAllResponse(
    val success: Boolean,
    val data: ReadAllData?,
    val message: String?
)

data class ReadAllData(
    val count: Int
)
