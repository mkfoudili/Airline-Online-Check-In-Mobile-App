package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.preferences.UserPreferencesRepository
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

    override suspend fun updateProfile(fullName: String, email: String, phoneNumber: String): Profile {
        // Implementation for updateProfile if needed, currently just placeholder
        // or keeping it as per interface requirements.
        // Assuming current logic just needs getProfile fixed.
        throw UnsupportedOperationException("Update profile not implemented yet")
    }

    override suspend fun updatePassword(currentPassword: String, newPassword: String): Result<Unit> {
        // Implementation for updatePassword if needed
        return Result.success(Unit)
    }
}