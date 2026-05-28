package com.example.domain.repository

import com.example.domain.model.Profile

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(fullName: String, email: String, phoneNumber: String): Profile
    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit>
}
