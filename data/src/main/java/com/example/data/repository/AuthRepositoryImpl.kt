package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.remote.dto.*
import com.example.data.remote.retrofit.Endpoint
import com.example.data.security.SecureStorage
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.validation.RegistrationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: Endpoint,
    private val userPrefs: UserPreferencesRepository,
    private val secureStorage: SecureStorage
) : AuthRepository {

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(request: RegistrationRequest, callback: (Result<User>) -> Unit) {
        scope.launch {
            try {
                val userDto = api.register(request)
                handleAuthSuccess(userDto, userDto.token)
                callback(Result.success(userDto.toDomain()))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun login(email: String, password: String, callback: (Result<User>) -> Unit) {
        scope.launch {
            try {
                val response = api.login(LoginRequest(email, password))
                handleAuthSuccess(response.user, response.token)
                callback(Result.success(response.user.toDomain()))
            } catch (e: Exception) {
                callback(Result.failure(Exception("Login failed: ${e.message}")))
            }
        }
    }

    override fun emailExists(email: String, callback: (Result<Boolean>) -> Unit) {
        scope.launch {
            try {
                val exists = api.emailExists(email)
                callback(Result.success(exists))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun logout(callback: () -> Unit) {
        scope.launch {
            try {
                api.logout()
            } catch (e: Exception) {
                // Silence errors during logout
            } finally {
                userPrefs.clearUser()
                secureStorage.clearTokens()
                callback()
            }
        }
    }

    override fun getCurrentUserId(): String? {
        return secureStorage.getUserId()
    }

    private suspend fun handleAuthSuccess(userDto: UserDto, token: String?) {
        userPrefs.saveUser(
            uid = userDto.uid,
            name = userDto.displayName ?: "",
            email = userDto.email ?: ""
        )
        secureStorage.saveUserId(userDto.uid)
        token?.let { secureStorage.saveAuthToken(it) }
    }
}
