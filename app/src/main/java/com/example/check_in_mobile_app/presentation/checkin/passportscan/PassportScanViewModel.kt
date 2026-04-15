package com.example.check_in_mobile_app.presentation.checkin.passportscan

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class ScanStatus { PENDING, SUCCESS }

data class PassportScanUiState(
    val capturedBitmap: Bitmap? = null,
    val scanStatus: ScanStatus = ScanStatus.PENDING
)

class PassportScanViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(PassportScanUiState())
    val uiState: StateFlow<PassportScanUiState> = _uiState.asStateFlow()

    /** Called when the camera or gallery returns a result. */
    fun onPassportCaptured(bitmap: Bitmap?) {
        _uiState.update { state ->
            state.copy(
                capturedBitmap = bitmap,
                scanStatus = if (bitmap != null) ScanStatus.SUCCESS else ScanStatus.PENDING
            )
        }
    }

    /** Resets to allow retake. */
    fun onClearScan() {
        _uiState.update { PassportScanUiState() }
    }
}
