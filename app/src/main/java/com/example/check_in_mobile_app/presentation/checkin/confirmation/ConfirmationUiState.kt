package com.example.check_in_mobile_app.presentation.checkin.confirmation

data class ConfirmationUiState(
    val isDownloadingPdf: Boolean = false,
    val showDownloadSuccess: Boolean = false,
    val errorMessage: String? = null
)