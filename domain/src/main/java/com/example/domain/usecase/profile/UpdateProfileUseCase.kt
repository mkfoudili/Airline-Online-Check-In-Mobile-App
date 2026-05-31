package com.example.domain.usecase.profile

import com.example.domain.model.Profile
import com.example.domain.model.SecurityLevel
import com.example.domain.repository.ProfileRepository
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor(
    private val repository: ProfileRepository
) {
    suspend operator fun invoke(
        fullName: String,
        email: String,
        phoneNumber: String
    ): Profile {
        return repository.updateProfile(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber
        )
    }
}
