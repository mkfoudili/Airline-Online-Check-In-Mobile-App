package com.example.domain.usecase.profile

import com.example.domain.model.Profile
import com.example.domain.model.SecurityLevel
import javax.inject.Inject

class GetProfileUseCase @Inject constructor() {
    operator fun invoke(): Profile {
        return Profile(
            fullName = "Djerfi Fatima",
            email = "mr_mekirch@esi.dz",
            phoneNumber = "+1 234 567 890",
            avatarUrl = "https://example.com/avatar.jpg",
            isVerified = true,
            securityLevel = SecurityLevel.HIGH,
            isOnline = true
        )
    }
}
