package com.example.check_in_mobile_app.presentation.profile

import com.example.domain.usecase.profile.GetProfileUseCase
import com.example.domain.usecase.profile.UpdateProfileUseCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase
): ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<ProfileUiAction>()
    val uiAction: SharedFlow<ProfileUiAction> = _uiAction.asSharedFlow()

    init {
        fetchProfile()
    }

    private fun fetchProfile() {
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

    private fun enterEditMode() {
        _uiState.value = _uiState.value.copy(
            isEditing = true,
            editedName = _uiState.value.name,
            editedEmail = _uiState.value.email,
            editedPhoneNumber = _uiState.value.phoneNumber
        )
    }

    fun onEvent(event: ProfileEvent) {
        viewModelScope.launch {
            when (event) {
                ProfileEvent.OnEditEmailClicked -> {
                    enterEditMode()
                }
                ProfileEvent.OnEditPhoneClicked -> {
                    enterEditMode()
                }
                ProfileEvent.OnEditPasswordClicked -> {
                    _uiAction.emit(ProfileUiAction.NavigateToEditPassword)
                }
                ProfileEvent.OnEditProfileClicked -> {
                    enterEditMode()
                }
                is ProfileEvent.OnNameChanged -> {
                    _uiState.value = _uiState.value.copy(editedName = event.name)
                }
                is ProfileEvent.OnEmailChanged -> {
                    _uiState.value = _uiState.value.copy(editedEmail = event.email)
                }
                is ProfileEvent.OnPhoneNumberChanged -> {
                    _uiState.value = _uiState.value.copy(editedPhoneNumber = event.phoneNumber)
                }
                ProfileEvent.OnSaveClicked -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    try {
                        val updatedProfile = updateProfileUseCase(
                            fullName = _uiState.value.editedName,
                            email = _uiState.value.editedEmail,
                            phoneNumber = _uiState.value.editedPhoneNumber
                        )
                        _uiState.value = _uiState.value.copy(
                            isEditing = false,
                            isLoading = false,
                            name = updatedProfile.fullName,
                            email = updatedProfile.email,
                            phoneNumber = updatedProfile.phoneNumber
                        )
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Update failed"
                        )
                    }
                }
                ProfileEvent.OnCancelClicked -> {
                    _uiState.value = _uiState.value.copy(isEditing = false)
                }
                ProfileEvent.OnBackClicked -> {
                    if (_uiState.value.isEditing) {
                        _uiState.value = _uiState.value.copy(isEditing = false)
                    } else {
                        _uiAction.emit(ProfileUiAction.NavigateBack)
                    }
                }
                ProfileEvent.OnChangePhotoClicked -> {
                    // Handle photo change
                }
            }
        }
    }
}
