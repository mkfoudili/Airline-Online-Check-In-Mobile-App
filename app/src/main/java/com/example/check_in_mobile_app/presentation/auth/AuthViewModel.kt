package com.example.check_in_mobile_app.presentation.auth
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.presentation.utils.toUserFriendlyMessage
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.validation.RegistrationRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    var isLoggedOut by mutableStateOf(false)
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
                        errorMessage = it.toUserFriendlyMessage()
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
                        errorMessage = it.toUserFriendlyMessage()
                    )
                }
            }
        }
    }


    fun signInWithGoogle(idToken: String) {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        authRepository.loginWithGoogle(idToken) { result ->
            viewModelScope.launch {
                result.onSuccess {
                    uiState = uiState.copy(isLoading = false, isSuccess = true)
                }.onFailure {
                    uiState = uiState.copy(
                        isLoading = false,
                        errorMessage = it.toUserFriendlyMessage()
                    )
                }
            }
        }
    }

    fun onLoginError() {
        uiState = uiState.copy(errorMessage = null)
    }

    fun setError(message: String) {
        uiState = uiState.copy(errorMessage = message, isLoading = false)
    }

    fun onLogout() {
        authRepository.logout {
            viewModelScope.launch {
                isLoggedOut = true
            }
        }
    }
}
