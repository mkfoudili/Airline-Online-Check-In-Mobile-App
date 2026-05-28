package com.example.check_in_mobile_app.presentation.main.profile

import com.example.domain.model.SecurityLevel

data class ProfileUiState(
    // --- Profile view data ---
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val language: String = "English",
    val isDarkMode: Boolean = false,
    val passwordMasked: String = "************",
    val profileImageUrl: String? = null,
    val isVerified: Boolean = false,
    val securityLevel: SecurityLevel = SecurityLevel.HIGH,
    val isOnline: Boolean = false,

    // --- Edit mode ---
    val isEditing: Boolean = false,
    val editedName: String = "",
    val editedEmail: String = "",
    val editedPhoneNumber: String = "",
    val editedLanguage: String = "English",
    val isLanguageDropdownExpanded: Boolean = false,

    // --- Change password mode ---
    val isChangingPassword: Boolean = false,
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false,

    // --- Logout ---
    val showLogoutDialog: Boolean = false,

    // --- Async state ---
    val isLoading: Boolean = false,
    val error: String? = null
)
