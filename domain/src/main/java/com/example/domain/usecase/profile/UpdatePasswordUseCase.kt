package com.example.domain.usecase.profile

import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdatePasswordUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(currentPassword: String, newPassword: String): Result<Unit> {
        return repository.updatePassword(currentPassword, newPassword)
    }
}
