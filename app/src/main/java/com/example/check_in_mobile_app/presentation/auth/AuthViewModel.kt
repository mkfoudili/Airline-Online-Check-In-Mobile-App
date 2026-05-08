package com.example.check_in_mobile_app.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.validation.RegistrationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val userPrefs: UserPreferencesRepository
) : ViewModel() {

    val isLoggedIn = userPrefs.isLoggedInFlow

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun register(name: String, email: String, phone: String, password: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        val request = RegistrationRequest(
            uid = java.util.UUID.randomUUID().toString(),
            email = email,
            displayName = name,
            phoneNumber = phone,
            password = password
        )

        authRepository.register(request) { result ->
            viewModelScope.launch {
                result.onSuccess {
                    uiState = uiState.copy(isLoading = false, isSuccess = true)
                }.onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = it.message ?: "Registration failed"
                    )
                }
            }
        }
    }

    fun login(email: String, password: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        authRepository.login(email, password) { result ->
            viewModelScope.launch {
                result.onSuccess {
                    uiState = uiState.copy(isLoading = false, isSuccess = true)
                }.onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = it.message ?: "Login failed"
                    )
                }
            }
        }
    }

    fun signInWithGoogle(idToken: String) {
        // Implementation for Google Sign-In would go here
    }

    fun onLoginError() {
        uiState = uiState.copy(errorMessage = null)
    }

    fun onLogout() {
        authRepository.logout {
            // Logout completed
        }
    }
}
