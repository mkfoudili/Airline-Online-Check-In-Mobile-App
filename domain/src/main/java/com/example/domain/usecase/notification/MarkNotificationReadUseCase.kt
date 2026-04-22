package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class MarkNotificationReadUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(notificationId: String, callback: (Result<Unit>) -> Unit) {
        repository.markAsRead(notificationId, callback)
    }
}
