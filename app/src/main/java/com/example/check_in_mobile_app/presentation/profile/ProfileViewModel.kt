package com.example.check_in_mobile_app.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.profile.GetProfileUseCase
import com.example.domain.usecase.profile.UpdatePasswordUseCase
import com.example.domain.usecase.profile.UpdateProfileUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val getProfileUseCase: GetProfileUseCase = GetProfileUseCase(),
    private val updateProfileUseCase: UpdateProfileUseCase = UpdateProfileUseCase(),
    private val updatePasswordUseCase: UpdatePasswordUseCase = UpdatePasswordUseCase()
) : ViewModel() {
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

                _uiState.value = _uiState.value.copy(
                    profileData = ProfileData(
                        name = profile.fullName,
                        email = profile.email,
                        phoneNumber = profile.phoneNumber,
                        passwordMasked = "************",
                        profileImageUrl = profile.avatarUrl,
                        isVerified = profile.isVerified,
                        securityLevel = profile.securityLevel,
                        isOnline = profile.isOnline,
                    ),
                    isLoading = false
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
            isChangingPassword = false,
            editData = EditProfileData(
                name = _uiState.value.profileData.name,
                email = _uiState.value.profileData.email,
                phoneNumber = _uiState.value.profileData.phoneNumber
            )
        )
    }

    private fun enterChangePasswordMode() {
        _uiState.value = _uiState.value.copy(
            isChangingPassword = true,
            isEditing = false,
            changePasswordData = ChangePasswordData()
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
                    enterChangePasswordMode()
                }
                ProfileEvent.OnEditProfileClicked -> {
                    enterEditMode()
                }
                is ProfileEvent.OnNameChanged -> {
                    _uiState.value = _uiState.value.copy(
                        editData = _uiState.value.editData.copy(name = event.name)
                    )
                }
                is ProfileEvent.OnEmailChanged -> {
                    _uiState.value = _uiState.value.copy(
                        editData = _uiState.value.editData.copy(email = event.email)
                    )
                }
                is ProfileEvent.OnPhoneNumberChanged -> {
                    _uiState.value = _uiState.value.copy(
                        editData = _uiState.value.editData.copy(phoneNumber = event.phoneNumber)
                    )
                }
                ProfileEvent.OnSaveClicked -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                    try {
                        val updatedProfile = updateProfileUseCase(
                            fullName = _uiState.value.editData.name,
                            email = _uiState.value.editData.email,
                            phoneNumber = _uiState.value.editData.phoneNumber
                        )
                        _uiState.value = _uiState.value.copy(
                            isEditing = false,
                            isLoading = false,
                            profileData = _uiState.value.profileData.copy(
                                name = updatedProfile.fullName,
                                email = updatedProfile.email,
                                phoneNumber = updatedProfile.phoneNumber
                            )
                        )
                    } catch (e: Exception) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = e.message ?: "Update failed"
                        )
                    }
                }
                ProfileEvent.OnCancelClicked -> {
                    _uiState.value = _uiState.value.copy(
                        isEditing = false,
                        isChangingPassword = false
                    )
                }
                ProfileEvent.OnBackClicked -> {
                    if (_uiState.value.isEditing || _uiState.value.isChangingPassword) {
                        _uiState.value = _uiState.value.copy(
                            isEditing = false,
                            isChangingPassword = false
                        )
                    } else {
                        _uiAction.emit(ProfileUiAction.NavigateBack)
                    }
                }
                ProfileEvent.OnChangePhotoClicked -> {
                    // Handle photo change
                }
                is ProfileEvent.OnCurrentPasswordChanged -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(currentPassword = event.value)
                    )
                }
                is ProfileEvent.OnNewPasswordChanged -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(newPassword = event.value)
                    )
                }
                is ProfileEvent.OnConfirmPasswordChanged -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(confirmPassword = event.value)
                    )
                }
                ProfileEvent.OnToggleCurrentPasswordVisibility -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(
                            isCurrentPasswordVisible = !_uiState.value.changePasswordData.isCurrentPasswordVisible
                        )
                    )
                }
                ProfileEvent.OnToggleNewPasswordVisibility -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(
                            isNewPasswordVisible = !_uiState.value.changePasswordData.isNewPasswordVisible
                        )
                    )
                }
                ProfileEvent.OnToggleConfirmPasswordVisibility -> {
                    _uiState.value = _uiState.value.copy(
                        changePasswordData = _uiState.value.changePasswordData.copy(
                            isConfirmPasswordVisible = !_uiState.value.changePasswordData.isConfirmPasswordVisible
                        )
                    )
                }
                ProfileEvent.OnSavePasswordClicked -> {
                    val passwordData = _uiState.value.changePasswordData
                    if (passwordData.newPassword != passwordData.confirmPassword) {
                        _uiState.value = _uiState.value.copy(error = "Passwords do not match")
                        return@launch
                    }

                    _uiState.value = _uiState.value.copy(isLoading = true)
                    val result = updatePasswordUseCase(
                        currentPassword = passwordData.currentPassword,
                        newPassword = passwordData.newPassword
                    )
                    
                    if (result.isSuccess) {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            isChangingPassword = false,
                            error = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            error = result.exceptionOrNull()?.message ?: "Update failed"
                        )
                    }
                }
            }
        }
    }
}
