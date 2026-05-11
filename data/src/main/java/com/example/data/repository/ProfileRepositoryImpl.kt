package com.example.data.repository

import com.example.data.preferences.UserPreferencesRepository
import com.example.data.remote.retrofit.Endpoint
import com.example.data.security.SecureStorage
import com.example.domain.model.Profile
import com.example.domain.repository.ProfileRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val api: Endpoint,
    private val userPrefs: UserPreferencesRepository,
    private val secureStorage: SecureStorage
): ProfileRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun getProfile(callback: (Result<Profile>) -> Unit) {

    }
    override fun updateProfile(profile: Profile, callback: (Result<Unit>) -> Unit) {

    }
    override fun changePassword(currentPassword: String, newPassword: String, callback: (Result<Unit>) -> Unit) {

    }
    override fun changeEmail(newEmail: String, callback: (Result<Unit>) -> Unit) {

    }
    override fun changeAvatar(avatarUri: String, callback: (Result<Unit>) -> Unit) {

    }
}