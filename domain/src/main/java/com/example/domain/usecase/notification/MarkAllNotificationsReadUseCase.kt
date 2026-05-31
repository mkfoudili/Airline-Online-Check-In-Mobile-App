package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * Use case to mark all notifications as read for a specific user.
 */
class MarkAllNotificationsReadUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(uid: String): Result<Unit> {
        return repository.markAllAsRead(uid)
    }
}
