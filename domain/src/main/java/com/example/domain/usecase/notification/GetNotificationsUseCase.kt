package com.example.domain.usecase.notification

import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * Use case to fetch all notifications for a user from the repository.
 */
class GetNotificationsUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(uid: String): Result<List<Notification>> {
        return repository.getNotifications(uid)
    }
}
