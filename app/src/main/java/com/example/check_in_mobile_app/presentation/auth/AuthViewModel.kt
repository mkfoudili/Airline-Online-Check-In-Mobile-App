package com.example.check_in_mobile_app.presentation.auth

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

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
        uiState = uiState.copy(isLoading = true)

        viewModelScope.launch {
            delay(1000)
            uiState = uiState.copy(isLoading = false, isSuccess = true)
        }
    }
}