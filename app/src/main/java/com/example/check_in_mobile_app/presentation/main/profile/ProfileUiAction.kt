package com.example.check_in_mobile_app.presentation.main.profile

sealed class ProfileUiAction {
    object NavigateBack : ProfileUiAction()
    data class ChangeLanguage(val languageCode: String) : ProfileUiAction()
    object Logout : ProfileUiAction()
}
