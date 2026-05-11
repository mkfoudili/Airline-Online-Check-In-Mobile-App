package com.example.domain.usecase.profile

import com.example.domain.model.Profile
import com.example.domain.model.SecurityLevel
import javax.inject.Inject

class UpdateProfileUseCase @Inject constructor() {
    operator fun invoke(fullName: String, email: String, phoneNumber: String): Profile {
        return Profile(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber,
            avatarUrl = "https://example.com/avatar.jpg",
            isVerified = true,
            securityLevel = SecurityLevel.HIGH,
            isOnline = true
        )
    }
}
