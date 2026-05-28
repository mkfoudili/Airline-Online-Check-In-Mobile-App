package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.remote.dto.UpdatePasswordRequest
import com.example.data.remote.dto.UpdateProfileRequest
import com.example.data.remote.retrofit.Endpoint
import com.example.data.security.SecureStorage
import com.example.domain.model.Profile
import com.example.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: Endpoint,
    private val userPrefs: UserPreferencesRepository,
    private val secureStorage: SecureStorage
): ProfileRepository {

    override suspend fun getProfile(): Profile = withContext(Dispatchers.IO) {
        val token = secureStorage.getAuthToken() ?: throw Exception("Not authenticated")
        
        val response = api.getProfile("Bearer $token")
        
        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response body")
            if (body.success && body.data != null) {
                return@withContext body.data.toDomain()
            } else {
                throw Exception(body.message ?: "Unknown error")
            }
        } else {
            throw Exception("Error code: ${response.code()}")
        }
    }

    override suspend fun updateProfile(
        fullName: String,
        email: String,
        phoneNumber: String
    ): Profile = withContext(Dispatchers.IO) {
        val token = secureStorage.getAuthToken() ?: throw Exception("Not authenticated")

        val request = UpdateProfileRequest(
            fullName = fullName,
            email = email,
            phoneNumber = phoneNumber
        )

        val response = api.updateProfile("Bearer $token", request)

        if (response.isSuccessful) {
            val body = response.body() ?: throw Exception("Empty response body")

            if (body.success && body.data != null) {
                return@withContext body.data.toDomain()
            } else {
                throw Exception(body.message ?: "Failed to update profile")
            }
        } else {
            val errorMsg = response.errorBody()?.string() ?: "Error code: ${response.code()}"
            throw Exception(errorMsg)
        }
    }

    override suspend fun updatePassword(
        currentPassword: String,
        newPassword: String
    ): Result<Unit> = withContext(Dispatchers.IO) {
        return@withContext try {
            val token = secureStorage.getAuthToken()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val request = UpdatePasswordRequest(
                currentPassword = currentPassword,
                newPassword = newPassword
            )

            val response = api.updatePassword("Bearer $token", request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to update password"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}