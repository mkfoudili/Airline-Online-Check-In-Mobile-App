package com.example.check_in_mobile_app.presentation.main.profile

sealed class ProfileEvent {
    object OnEditEmailClicked : ProfileEvent()
    object OnEditPhoneClicked : ProfileEvent()
    object OnEditPasswordClicked : ProfileEvent()
    object OnEditProfileClicked : ProfileEvent()
    data class OnNameChanged(val name: String) : ProfileEvent()
    data class OnEmailChanged(val email: String) : ProfileEvent()
    data class OnPhoneNumberChanged(val phoneNumber: String) : ProfileEvent()
    data class OnLanguageChanged(val language: String) : ProfileEvent()
    object OnToggleLanguageDropdown : ProfileEvent()
    data class OnThemeToggled(val isDarkMode: Boolean) : ProfileEvent()
    object OnSaveClicked : ProfileEvent()
    object OnCancelClicked : ProfileEvent()
    object OnBackClicked : ProfileEvent()
    object OnChangePhotoClicked : ProfileEvent()
    data class OnCurrentPasswordChanged(val value: String) : ProfileEvent()
    data class OnNewPasswordChanged(val value: String) : ProfileEvent()
    data class OnConfirmPasswordChanged(val value: String) : ProfileEvent()
    object OnToggleCurrentPasswordVisibility : ProfileEvent()
    object OnToggleNewPasswordVisibility : ProfileEvent()
    object OnToggleConfirmPasswordVisibility : ProfileEvent()
    object OnSavePasswordClicked : ProfileEvent()

    // Logout Events
    object OnLogoutClicked : ProfileEvent()
    object OnLogoutConfirmClicked : ProfileEvent()
    object OnLogoutDismissClicked : ProfileEvent()
}
