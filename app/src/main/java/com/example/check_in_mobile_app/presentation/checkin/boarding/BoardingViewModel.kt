package com.example.check_in_mobile_app.presentation.checkin.boarding

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BoardingViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(BoardingUiState())
    val uiState: StateFlow<BoardingUiState> = _uiState.asStateFlow()

    fun onDownloadPdf() {
        _uiState.update { it.copy(isLoading = true) }
        // TODO: call GeneratePdfUseCase and handle result
        _uiState.update { it.copy(isLoading = false, isPdfGenerated = true, showDownloadSuccess = true) }
    }

    fun onDismissDownloadSuccess() {
        _uiState.update { it.copy(showDownloadSuccess = false) }
    }

    fun onConnectivityChanged(isConnected: Boolean) {
        _uiState.update { it.copy(isOffline = !isConnected) }
    }
}