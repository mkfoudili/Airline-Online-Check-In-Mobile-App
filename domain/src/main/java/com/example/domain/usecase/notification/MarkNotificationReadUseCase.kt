package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * Use case to mark a single notification as read.
 */
class MarkNotificationReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(notificationId: String): Result<Unit> {
        return repository.markAsRead(notificationId)
    }
}
