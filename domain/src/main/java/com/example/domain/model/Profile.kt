package com.example.domain.model

data class Profile(
    val fullName: String,
    val email: String,
    val phoneNumber: String,
    val avatarUrl: String?,
    val isVerified: Boolean,
    val securityLevel: SecurityLevel,
    val isOnline: Boolean
)

enum class SecurityLevel {
    LOW, MEDIUM, HIGH
}
