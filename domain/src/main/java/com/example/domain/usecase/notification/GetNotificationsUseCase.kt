package com.example.domain.usecase.notification

import com.example.domain.model.Notification
import com.example.domain.model.NotificationType
import com.example.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        val now = System.currentTimeMillis()
        val hour = 3600000L
        val day = 86400000L

        val mockNotifications = listOf(
            Notification(
                notificationId = "1",
                passengerId = uid,
                title = "Check-in Successful",
                body = "Your check-in for flight AA123 was successful. You can now view your boarding pass.",
                type = NotificationType.CHECK_IN_CONFIRMATION,
                isRead = false,
                createdAt = now - (2 * hour)
            ),
            Notification(
                notificationId = "2",
                passengerId = uid,
                title = "Boarding Soon",
                body = "Boarding for flight AA123 starts in 30 minutes at Gate B12. Please proceed to the gate.",
                type = NotificationType.BOARDING_REMINDER,
                isRead = false,
                createdAt = now - (5 * hour)
            ),
            Notification(
                notificationId = "3",
                passengerId = uid,
                title = "Gate Change",
                body = "Flight AA123 gate has been changed from B12 to C05.",
                type = NotificationType.GATE_CHANGE,
                isRead = true,
                createdAt = now - (26 * hour)
            ),
            Notification(
                notificationId = "4",
                passengerId = uid,
                title = "Flight Delay",
                body = "Flight AA123 is delayed by 45 minutes due to weather conditions.",
                type = NotificationType.DELAY,
                isRead = true,
                createdAt = now - (3 * day)
            ),
            Notification(
                notificationId = "5",
                passengerId = uid,
                title = "Flight Status Updated",
                body = "Your flight AA123 status has been updated.",
                type = NotificationType.FLIGHT_STATUS_UPDATE,
                isRead = true,
                createdAt = now - (10 * day)
            )
        )
        callback(Result.success(mockNotifications))
    }
}
