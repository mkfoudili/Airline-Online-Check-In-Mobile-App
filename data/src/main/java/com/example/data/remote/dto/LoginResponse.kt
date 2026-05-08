package com.example.data.remote.dto

data class LoginResponse(
    val user: UserDto,
    val token: String
)
