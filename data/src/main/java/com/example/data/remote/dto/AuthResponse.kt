package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class AuthResponse(
    @SerializedName("user")         val user: UserDto,
    @SerializedName("token")        val token: String,
    @SerializedName("refreshToken") val refreshToken: String? = null
)