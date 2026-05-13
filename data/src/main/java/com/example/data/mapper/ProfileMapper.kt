package com.example.data.mapper

import com.example.data.remote.dto.ProfileDto
import com.example.domain.model.Profile
import com.example.domain.model.SecurityLevel

fun ProfileDto.toDomain(): Profile {
    return Profile(
        fullName = this.fullName,
        email = this.email,
        phoneNumber = this.phoneNumber ?: "",
        avatarUrl = this.avatarUrl,
        isVerified = this.isVerified,
        securityLevel = SecurityLevel.MEDIUM, // Default value as it's not in the DTO
        isOnline = true // Default value
    )
}
