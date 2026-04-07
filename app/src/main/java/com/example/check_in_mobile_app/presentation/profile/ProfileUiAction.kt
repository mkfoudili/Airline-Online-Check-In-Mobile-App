package com.example.check_in_mobile_app.presentation.profile

sealed class ProfileUiAction {
    object NavigateToEditEmail : ProfileUiAction()
    object NavigateToEditPhone : ProfileUiAction()
    object NavigateToEditPassword : ProfileUiAction()
}