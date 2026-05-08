package com.example.data.repository

import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.validation.RegistrationRequest
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MockAuthRepositoryImpl @Inject constructor(
    private val userPrefs: com.example.data.preferences.UserPreferencesRepository,
    private val secureStorage: com.example.data.security.SecureStorage
) : AuthRepository {

    override fun getCurrentUserId(): String? {
        return secureStorage.getUserId()
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    override fun register(request: RegistrationRequest, callback: (Result<User>) -> Unit) {
        scope.launch {
            delay(1000)
            val mockUser = User(
                uid = request.uid.ifEmpty { "mock_uid_${System.currentTimeMillis()}" },
                email = request.email,
                displayName = request.displayName,
                phoneNumber = request.phoneNumber,
                provider = "email"
            )
            handleAuthSuccess(mockUser, "mock_token")
            callback(Result.success(mockUser))
        }
    }

    override fun login(email: String, password: String, callback: (Result<User>) -> Unit) {
        scope.launch {
            delay(1000)
            if (email == "test@example.com" && password == "password") {
                val mockUser = User(
                    uid = "user-fatma-001",
                    email = email,
                    displayName = "Mock User",
                    phoneNumber = "123456789",
                    provider = "email"
                )
                handleAuthSuccess(mockUser, "mock_token")
                callback(Result.success(mockUser))
            } else {
                callback(Result.failure(Exception("Invalid email or password (use test@example.com / password)")))
            }
        }
    }

    private suspend fun handleAuthSuccess(user: User, token: String) {
        userPrefs.saveUser(
            uid = user.uid,
            name = user.displayName ?: "User",
            email = user.email
        )
        secureStorage.saveUserId(user.uid)
        secureStorage.saveAuthToken(token)
    }

    override fun emailExists(email: String, callback: (Result<Boolean>) -> Unit) {
        scope.launch {
            delay(500)
            callback(Result.success(email == "test@example.com"))
        }
    }

    override fun logout(callback: () -> Unit) {
        scope.launch {
            delay(200)
            userPrefs.clearUser()
            secureStorage.clearTokens()
            callback()
        }
    }
}
