package com.example.domain.usecase.notification

import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository

class GetNotificationsUseCase(
    private val repository: NotificationRepository
) {
    operator fun invoke(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        repository.getNotifications(uid, callback)
    }
}