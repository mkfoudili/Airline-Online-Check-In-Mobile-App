package com.example.check_in_mobile_app.presentation.profile

import com.example.domain.model.SecurityLevel

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val passwordMasked: String = "************",
    val profileImageUrl: String? = null,
    val isVerified: Boolean = false,
    val securityLevel: SecurityLevel = SecurityLevel.HIGH,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val errorMessage: String? = null,
    val error: String? = null,
    val isEditing: Boolean = false,
    val editedName: String = "",
    val editedEmail: String = "",
    val editedPhoneNumber: String = ""
)
