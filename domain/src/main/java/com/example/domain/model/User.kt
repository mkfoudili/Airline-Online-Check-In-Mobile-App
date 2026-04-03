package com.example.domain.model

data class User(
    val uid: String,
    val email: String,
    val displayName: String?,
    val phoneNumber: String?,
    val provider: String?
)
