package com.example.data.remote.dto

data class ProfileResponse(
    val success: Boolean,
    val data: ProfileDto?,
    val message: String?
)

data class ProfileDto(
    val uid: String,
    val email: String,
    val fullName: String,
    val phoneNumber: String?,
    val avatarUrl: String?,
    val isVerified: Boolean
)

data class UpdateProfileRequest(
    val fullName: String,
    val email: String,
    val phoneNumber: String
)