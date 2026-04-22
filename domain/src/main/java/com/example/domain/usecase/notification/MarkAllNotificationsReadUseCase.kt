package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository

class MarkAllNotificationsReadUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(uid: String, callback: (Result<Unit>) -> Unit) {
        repository.markAllAsRead(uid, callback)
    }
}
