package com.example.domain.usecase.notification

import com.example.domain.model.Notification
import com.example.domain.model.NotificationType
import com.example.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        // Mocking behavior: returning a static list of notifications instead of calling the repository
        val mockNotifications = listOf(
            Notification(
                notificationId = "1",
                passengerId = uid,
                title = "Check-in Successful",
                body = "Your check-in for flight AA123 was successful. You can now view your boarding pass.",
                type = NotificationType.CHECK_IN_CONFIRMATION,
                isRead = false,
                createdAt = System.currentTimeMillis() - 3600000 // 1 hour ago
            ),
            Notification(
                notificationId = "2",
                passengerId = uid,
                title = "Boarding Soon",
                body = "Boarding for flight AA123 starts in 30 minutes at Gate B12. Please proceed to the gate.",
                type = NotificationType.BOARDING_REMINDER,
                isRead = false,
                createdAt = System.currentTimeMillis() - 7200000 // 2 hours ago
            ),
            Notification(
                notificationId = "3",
                passengerId = uid,
                title = "Gate Change",
                body = "Flight AA123 gate has been changed from B12 to C05.",
                type = NotificationType.GATE_CHANGE,
                isRead = true,
                createdAt = System.currentTimeMillis() - 10800000 // 3 hours ago
            ),
            Notification(
                notificationId = "4",
                passengerId = uid,
                title = "Flight Delay",
                body = "Flight AA123 is delayed by 45 minutes due to weather conditions.",
                type = NotificationType.DELAY,
                isRead = true,
                createdAt = System.currentTimeMillis() - 86400000 // 1 day ago
            )
        )
        callback(Result.success(mockNotifications))
    }
}
