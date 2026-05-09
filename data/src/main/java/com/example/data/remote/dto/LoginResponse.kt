package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    val user: UserDto,
    val token: String,
    @SerializedName("refreshToken") val refreshToken: String? = null
)
