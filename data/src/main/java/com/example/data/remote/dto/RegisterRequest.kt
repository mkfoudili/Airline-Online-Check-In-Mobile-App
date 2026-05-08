package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class RegisterRequest(
    @SerializedName("uid") val uid: String,
    @SerializedName("email") val email: String,
    @SerializedName("password") val password: String,
    @SerializedName("display_name") val displayName: String?,
    @SerializedName("phone_number") val phoneNumber: String?
)
