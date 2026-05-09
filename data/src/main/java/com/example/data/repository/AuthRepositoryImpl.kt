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
                val dataRequest = RegisterRequest(
                    uid = request.uid,
                    email = request.email,
                    password = request.password,
                    displayName = request.displayName,
                    phoneNumber = request.phoneNumber
                )
                val response = api.register(dataRequest)
                handleAuthSuccess(response.user, response.token, response.refreshToken)  // ← refreshToken passé
                callback(Result.success(response.user.toDomain()))
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun login(email: String, password: String, callback: (Result<User>) -> Unit) {
        scope.launch {
            try {
                val response = api.login(LoginRequest(email, password))
                handleAuthSuccess(response.user, response.token, response.refreshToken)  // ← refreshToken passé
                callback(Result.success(response.user.toDomain()))
            } catch (e: Exception) {
                callback(Result.failure(Exception("Login failed: ${e.message}")))
            }
        }
    }

    // signature mise à jour + saveTokens
    private suspend fun handleAuthSuccess(userDto: UserDto, token: String?, refreshToken: String?) {
        userPrefs.saveUser(
            uid = userDto.uid,
            name = userDto.displayName ?: "",
            email = userDto.email ?: ""
        )
        secureStorage.saveUserId(userDto.uid)
        if (token != null && refreshToken != null) {
            secureStorage.saveTokens(token, refreshToken)
        } else {
            token?.let { secureStorage.saveAuthToken(it) }  // fallback si pas encore de refresh
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
            // 1. Clear local data first so the UI updates immediately
            userPrefs.clearUser()
            secureStorage.clearTokens()
            
            // 2. Notify the UI that logout is done
            callback()

            // 3. Try to notify the server in the background (optional/silent)
            try {
                api.logout()
            } catch (e: Exception) {
                // Ignore server errors during logout
            }
        }
    }

    override fun getCurrentUserId(): String? {
        return secureStorage.getUserId()
    }
}
