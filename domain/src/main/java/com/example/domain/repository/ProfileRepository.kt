package com.example.domain.repository

import android.content.Context
import android.net.Uri
import com.example.domain.model.Profile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface ProfileRepository {
    suspend fun getProfile(): Profile
    suspend fun updateProfile(fullName: String, email: String, phoneNumber: String): Profile
    suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit>
    suspend fun uploadProfilePhoto(imageUri: Uri, context: Context): Profile
}
