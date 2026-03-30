package com.example.domain.validation

data class RegistrationRequest(
    val uid: String,
    val email: String,
    val password: String,
    val displayName: String?,
    val phoneNumber: String?
)