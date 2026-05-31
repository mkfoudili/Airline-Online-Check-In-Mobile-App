package com.example.domain.usecase.notification

import com.example.domain.repository.NotificationRepository
import javax.inject.Inject

/**
 * Use case to register the Firebase Cloud Messaging token with the backend.
 */
class RegisterFcmTokenUseCase @Inject constructor(
    private val repository: NotificationRepository
) {
    suspend operator fun invoke(token: String): Result<Unit> {
        return repository.registerToken(token)
    }
}
