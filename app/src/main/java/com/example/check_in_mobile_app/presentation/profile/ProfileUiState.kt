package com.example.check_in_mobile_app.presentation.profile

data class ProfileUiState(
    val name: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val passwordMasked: String = "************",
    val profileImageUrl: String? = null,
    val isVerified: Boolean = false,
    val securityLevel: com.example.domain.model.SecurityLevel = SecurityLevel.HIGH,
    val isLoading: Boolean = false,
    val isOnline: Boolean = false,
    val errorMessage: String? = null,
    val error: String? = null,
)

enum class SecurityLevel { LOW, MEDIUM, HIGH }
