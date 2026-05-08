package com.example.data.remote.dto

data class TokenResponse(
    val accessToken: String,
    val refreshToken: String? = null
)
