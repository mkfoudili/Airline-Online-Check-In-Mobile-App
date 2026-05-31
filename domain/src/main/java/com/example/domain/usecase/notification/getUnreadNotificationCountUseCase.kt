package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

class getUnreadNotificationCountUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(uid: String): Result<Int> {
        return repository.getNotifications(uid).map { notifications ->
            notifications.count { !it.isRead }
        }
    }
}
