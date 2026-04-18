package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class MarkNotificationReadUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(notificationId: String, callback: (Result<Unit>) -> Unit) {
        // Mocking behavior: immediately returning success
        callback(Result.success(Unit))
    }
}
