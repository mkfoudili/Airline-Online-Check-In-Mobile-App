package com.example.check_in_mobile_app.presentation.checkin.confirmation

import com.example.domain.model.BoardingPass

data class ConfirmationUiState(
    val isGenerating: Boolean = false,
    val boardingPass: BoardingPass? = null,
    val isDownloadingPdf: Boolean = false,
    val showDownloadSuccess: Boolean = false,
    val errorMessage: String? = null
)