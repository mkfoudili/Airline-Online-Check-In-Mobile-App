package com.example.domain.usecase.profile

import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor() {
    operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> {
        return if (newPassword.length >= 8) {
            Result.success(Unit)
        } else {
            Result.failure(Exception("Password must be at least 8 characters long"))
        }
    }
}
