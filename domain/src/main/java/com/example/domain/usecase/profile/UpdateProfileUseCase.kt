package com.example.domain.usecase.profile

import com.example.domain.model.Profile
import com.example.domain.model.SecurityLevel

class UpdateProfileUseCase {
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
