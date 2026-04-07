package com.example.check_in_mobile_app.presentation.profile

sealed class ProfileUiAction {
    object NavigateToEditPassword : ProfileUiAction()
    object NavigateBack : ProfileUiAction()
}