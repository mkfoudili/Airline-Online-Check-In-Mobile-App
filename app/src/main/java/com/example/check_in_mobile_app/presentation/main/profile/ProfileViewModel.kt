package com.example.check_in_mobile_app.presentation.main.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.domain.usecase.profile.GetProfileUseCase
import com.example.domain.usecase.profile.UpdatePasswordUseCase
import com.example.domain.usecase.profile.UpdateProfileUseCase
import com.example.domain.usecase.language.ChangeLanguageUseCase
import com.example.domain.usecase.language.GetSavedLanguageUseCase
import com.example.check_in_mobile_app.AppContainer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val changeLanguageUseCase: ChangeLanguageUseCase,
    private val getSavedLanguageUseCase: GetSavedLanguageUseCase,
    private val getProfileUseCase: GetProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
    private val updatePasswordUseCase: UpdatePasswordUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _uiAction = MutableSharedFlow<ProfileUiAction>()
    val uiAction: SharedFlow<ProfileUiAction> = _uiAction.asSharedFlow()

    init {
        fetchProfile()
        observeLanguage()
    }

    private fun observeLanguage() {
        viewModelScope.launch {
            getSavedLanguageUseCase().collectLatest { langTag ->
                val languageName = mapTagToLanguage(langTag ?: "en")
                _uiState.value = _uiState.value.copy(
                    profileData = _uiState.value.profileData.copy(language = languageName)
                )
            }
        }
    }

    private fun fetchProfile() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            try {
                val profile = getProfileUseCase()

                _uiState.value = _uiState.value.copy(
                    profileData = _uiState.value.profileData.copy(
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
            screenMode = ProfileScreenMode.EDIT,
            editData = EditProfileData(
                name = _uiState.value.profileData.name,
                email = _uiState.value.profileData.email,
                phoneNumber = _uiState.value.profileData.phoneNumber,
                language = _uiState.value.profileData.language
            )
        )
    }

    private fun enterChangePasswordMode() {
        _uiState.value = _uiState.value.copy(
            screenMode = ProfileScreenMode.CHANGE_PASSWORD,
            changePasswordData = ChangePasswordData()
        )
    }

    fun onEvent(event: ProfileEvent) {
        when (event) {
            ProfileEvent.OnEditEmailClicked,
            ProfileEvent.OnEditPhoneClicked,
            ProfileEvent.OnEditProfileClicked -> enterEditMode()
            ProfileEvent.OnEditPasswordClicked -> enterChangePasswordMode()
            is ProfileEvent.OnNameChanged -> onNameChanged(event.name)
            is ProfileEvent.OnEmailChanged -> onEmailChanged(event.email)
            is ProfileEvent.OnPhoneNumberChanged -> onPhoneNumberChanged(event.phoneNumber)
            is ProfileEvent.OnLanguageChanged -> onLanguageChanged(event.language)
            ProfileEvent.OnToggleLanguageDropdown -> toggleLanguageDropdown()
            ProfileEvent.OnSaveClicked -> onSaveClicked()
            ProfileEvent.OnCancelClicked -> onCancelClicked()
            ProfileEvent.OnBackClicked -> onBackClicked()
            ProfileEvent.OnChangePhotoClicked -> onChangePhotoClicked()
            is ProfileEvent.OnCurrentPasswordChanged -> onCurrentPasswordChanged(event.value)
            is ProfileEvent.OnNewPasswordChanged -> onNewPasswordChanged(event.value)
            is ProfileEvent.OnConfirmPasswordChanged -> onConfirmPasswordChanged(event.value)
            ProfileEvent.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            ProfileEvent.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            ProfileEvent.OnToggleConfirmPasswordVisibility -> toggleConfirmPasswordVisibility()
            ProfileEvent.OnSavePasswordClicked -> onSavePasswordClicked()
        }
    }

    private fun onNameChanged(name: String) {
        _uiState.value = _uiState.value.copy(
            editData = _uiState.value.editData.copy(name = name)
        )
    }

    private fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(
            editData = _uiState.value.editData.copy(email = email)
        )
    }

    private fun onPhoneNumberChanged(phoneNumber: String) {
        _uiState.value = _uiState.value.copy(
            editData = _uiState.value.editData.copy(phoneNumber = phoneNumber)
        )
    }

    private fun onLanguageChanged(language: String) {
        _uiState.value = _uiState.value.copy(
            editData = _uiState.value.editData.copy(
                language = language,
                isLanguageDropdownExpanded = false
            )
        )
    }

    private fun toggleLanguageDropdown() {
        _uiState.value = _uiState.value.copy(
            editData = _uiState.value.editData.copy(
                isLanguageDropdownExpanded = !_uiState.value.editData.isLanguageDropdownExpanded
            )
        )
    }

    private fun onSaveClicked() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            try {
                // Update basic profile info
                val updatedProfile = updateProfileUseCase(
                    fullName = _uiState.value.editData.name,
                    email = _uiState.value.editData.email,
                    phoneNumber = _uiState.value.editData.phoneNumber
                )

                // Update language if changed
                val selectedLanguage = _uiState.value.editData.language
                if (selectedLanguage != _uiState.value.profileData.language) {
                    val langTag = mapLanguageToTag(selectedLanguage)
                    changeLanguageUseCase(langTag)
                    _uiAction.emit(ProfileUiAction.ShowToast("Language updated to $selectedLanguage"))
                } else {
                    _uiAction.emit(ProfileUiAction.ShowToast("Profile updated successfully"))
                }

                _uiState.value = _uiState.value.copy(
                    screenMode = ProfileScreenMode.VIEW,
                    isLoading = false,
                    profileData = _uiState.value.profileData.copy(
                        name = updatedProfile.fullName,
                        email = updatedProfile.email,
                        phoneNumber = updatedProfile.phoneNumber,
                        language = selectedLanguage
                    )
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.message ?: "Update failed"
                )
            }
        }
    }

    private fun mapLanguageToTag(language: String): String {
        return when (language) {
            "French" -> "fr"
            "Arabic" -> "ar"
            else -> "en"
        }
    }

    private fun mapTagToLanguage(tag: String): String {
        return when (tag) {
            "fr" -> "French"
            "ar" -> "Arabic"
            else -> "English"
        }
    }

    private fun onCancelClicked() {
        _uiState.value = _uiState.value.copy(
            screenMode = ProfileScreenMode.VIEW
        )
    }

    private fun onBackClicked() {
        viewModelScope.launch {
            if (_uiState.value.screenMode != ProfileScreenMode.VIEW) {
                _uiState.value = _uiState.value.copy(
                    screenMode = ProfileScreenMode.VIEW
                )
            } else {
                _uiAction.emit(ProfileUiAction.NavigateBack)
            }
        }
    }

    private fun onChangePhotoClicked() {
        // Handle photo change
    }

    private fun onCurrentPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(currentPassword = value)
        )
    }

    private fun onNewPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(newPassword = value)
        )
    }

    private fun onConfirmPasswordChanged(value: String) {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(confirmPassword = value)
        )
    }

    private fun toggleCurrentPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(
                isCurrentPasswordVisible = !_uiState.value.changePasswordData.isCurrentPasswordVisible
            )
        )
    }

    private fun toggleNewPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(
                isNewPasswordVisible = !_uiState.value.changePasswordData.isNewPasswordVisible
            )
        )
    }

    private fun toggleConfirmPasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            changePasswordData = _uiState.value.changePasswordData.copy(
                isConfirmPasswordVisible = !_uiState.value.changePasswordData.isConfirmPasswordVisible
            )
        )
    }

    private fun onSavePasswordClicked() {
        viewModelScope.launch {
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
                _uiAction.emit(ProfileUiAction.ShowToast("Password changed successfully"))
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    screenMode = ProfileScreenMode.VIEW,
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

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>,
                extras: CreationExtras
            ): T {
                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                
                return ProfileViewModel(
                    changeLanguageUseCase = AppContainer.provideChangeLanguageUseCase(application),
                    getSavedLanguageUseCase = AppContainer.provideGetSavedLanguageUseCase(application),
                    getProfileUseCase = GetProfileUseCase(),
                    updateProfileUseCase = UpdateProfileUseCase(),
                    updatePasswordUseCase = UpdatePasswordUseCase()
                ) as T
            }
        }
    }
}
