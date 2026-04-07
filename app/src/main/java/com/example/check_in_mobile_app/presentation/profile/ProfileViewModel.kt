package com.example.check_in_mobile_app.presentation.profile

import com.example.domain.usecase.profile.GetProfileUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val getProfileUseCase: GetProfileUseCase): ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile(){
        fun fetchProfile() {
            viewModelScope.launch {
                _uiState.value = _uiState.value.copy(isLoading = true, error = null)

                try {
                    val profile = getProfileUseCase()

                    _uiState.value = ProfileUiState(
                        name = profile.fullName,
                        email = profile.email,
                        phoneNumber = profile.phoneNumber,
                        passwordMasked = "************",
                        profileImageUrl = profile.avatarUrl,
                        isVerified = profile.isVerified,
                        securityLevel = profile.securityLevel,
                        isLoading = false,
                        isOnline = profile.isOnline,
                    )

                } catch (e: Exception) {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Unknown error"
                    )
                }
            }
        }
    }
}