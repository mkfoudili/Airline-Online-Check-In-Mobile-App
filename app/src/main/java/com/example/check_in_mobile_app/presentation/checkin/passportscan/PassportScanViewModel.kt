package com.example.check_in_mobile_app.presentation.checkin.passportscan

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class PassportScanUiState(
    val capturedBitmap: Bitmap? = null
)

class PassportScanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PassportScanUiState())
    val uiState: StateFlow<PassportScanUiState> = _uiState.asStateFlow()

    fun onPassportCaptured(bitmap: Bitmap?) {
        _uiState.update { it.copy(capturedBitmap = bitmap) }
    }

    fun onClearScan() {
        _uiState.update { PassportScanUiState() }
    }
}
