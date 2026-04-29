package com.example.check_in_mobile_app.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.preferences.UserPreferencesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel (private val userPrefs: UserPreferencesRepository) : ViewModel() {

    val isLoggedIn = userPrefs.isLoggedInFlow

    var uiState by mutableStateOf(AuthUiState())
        private set

    fun register(name: String, email: String, phone: String, password: String) {
        // TODO: wire to AuthRepository once backend is ready
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            uiState = uiState.copy(isLoading = false, isSuccess = true)
        }
    }

    fun login(email: String, password: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {

            delay(1000)
                if (email == "test@gmail.com" && password == "test123") {
                  uiState = uiState.copy(isLoading = false, isSuccess = true)
                  onLoginSuccess("Test User", email)
                } else {
                    uiState = uiState.copy(
                        isLoading = false,
                        isSuccess = false,
                        errorMessage = "Invalid email or password"
                    )
                }


        }
    }

    fun signInWithGoogle(idToken: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            delay(1000)
            // TODO: replace with real repository call
            uiState = uiState.copy(isLoading = false, isSuccess = true)
        }
    }

    fun onLoginSuccess(name: String, email: String) {
        viewModelScope.launch {
            userPrefs.saveUser(name, email)
        }
    }

    fun onLoginError() {
        uiState = uiState.copy(errorMessage = null)
    }


    fun onLogout() {
        viewModelScope.launch {
            userPrefs.clearUser()
        }
    }
}


class AuthViewModelFactory(
    private val userPrefs: UserPreferencesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthViewModel(userPrefs) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}