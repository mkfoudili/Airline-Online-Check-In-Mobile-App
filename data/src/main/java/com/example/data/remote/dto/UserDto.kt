package com.example.data.remote.dto

import java.sql.Timestamp

data class UserDto(
    val uid: String,
    val email: String?,
    val displayName: String?,
    val phoneNumber: String?,
    val photoUrl: String?,
    val provider: String?,
    val createdAt: Timestamp?,
    val lastLogin: Timestamp?
)
