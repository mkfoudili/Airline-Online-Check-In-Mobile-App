package com.example.check_in_mobile_app.presentation.profile

import com.example.domain.model.SecurityLevel

data class ProfileUiState(
    val profileData: ProfileData = ProfileData(),
    val editData: EditProfileData = EditProfileData(),
    val changePasswordData: ChangePasswordData = ChangePasswordData(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isEditing: Boolean = false,
    val isChangingPassword: Boolean = false
)

data class ProfileData(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val passwordMasked: String = "************",
    val profileImageUrl: String? = null,
    val isVerified: Boolean = false,
    val securityLevel: SecurityLevel = SecurityLevel.HIGH,
    val isOnline: Boolean = false
)

data class EditProfileData(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = ""
)

data class ChangePasswordData(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isConfirmPasswordVisible: Boolean = false
)
