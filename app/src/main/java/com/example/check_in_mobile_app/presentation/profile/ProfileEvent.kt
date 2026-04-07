package com.example.check_in_mobile_app.presentation.profile

sealed class ProfileEvent {
    object OnEditEmailClicked : ProfileEvent()
    object OnEditPhoneClicked : ProfileEvent()
    object OnEditPasswordClicked : ProfileEvent()
}