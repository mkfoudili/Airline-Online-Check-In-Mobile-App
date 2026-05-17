package com.example.check_in_mobile_app.presentation.checkin

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Passenger
import com.example.domain.usecase.checkin.VerifyPassportUseCase
import com.example.domain.usecase.ocr.ExtractPassportDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OcrStatus { IDLE, SCANNING, VERIFYING, SUCCESS, ERROR }

data class CheckInSessionState(
    val verifiedPassenger: Passenger? = null,
    val ocrStatus: OcrStatus = OcrStatus.IDLE,
    val errorMessage: String? = null
)

/**
 * Shared ViewModel scoped to [CheckInActivity].
 * Holds the verified passenger across all check-in screens.
 *
 * Both PassportScanScreen (writes) and CheckingDetailsReviewScreen (reads) observe this VM.
 */
@HiltViewModel
class CheckInSessionViewModel @Inject constructor(
    private val extractPassportDataUseCase: ExtractPassportDataUseCase,
    private val verifyPassportUseCase: VerifyPassportUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(CheckInSessionState())
    val state: StateFlow<CheckInSessionState> = _state.asStateFlow()

    /**
     * Full OCR + verification pipeline:
     *  1. Run ML Kit OCR on [bitmap] → ParsedPassportData
     *  2. Call backend verify-passport with extracted passport# + lastName
     *  3. On success → store verified Passenger and set status = SUCCESS
     *  4. On failure → set status = ERROR with message
     */
    fun startOcrAndVerify(bitmap: Bitmap) {
        viewModelScope.launch {
            try {
                // Step 1: OCR
                _state.update { it.copy(ocrStatus = OcrStatus.SCANNING, errorMessage = null) }
    
                val parsed = extractPassportDataUseCase(bitmap)
                val ocrText = parsed?.rawText ?: ""
                println("OCR_DEBUG: Full raw text: $ocrText")
                
                val extractedPassportNumber = parsed?.passportNumber ?: ""
                val extractedLastName = parsed?.lastName ?: ""
    
                if (extractedPassportNumber.isBlank() || extractedLastName.isBlank()) {
                    val debugText = if (ocrText.length > 150) ocrText.take(150) + "..." else ocrText
                    _state.update {
                        it.copy(
                            ocrStatus = OcrStatus.ERROR,
                            errorMessage = "Could not read passport. (Read: $debugText). Please ensure the photo is clear."
                        )
                    }
                    return@launch
                }
    
                // Step 2: Backend verification
                _state.update { it.copy(ocrStatus = OcrStatus.VERIFYING) }
    
                val result = verifyPassportUseCase(
                    passportNumber = extractedPassportNumber,
                    lastName = extractedLastName,
                    firstName = parsed?.firstName,
                    nationality = parsed?.nationality,
                    dateOfBirth = parsed?.dateOfBirth,
                    expiryDate = parsed?.expiryDate
                )
    
                result.fold(
                    onSuccess = { passenger ->
                        _state.update {
                            it.copy(
                                ocrStatus = OcrStatus.SUCCESS,
                                verifiedPassenger = passenger,
                                errorMessage = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                ocrStatus = OcrStatus.ERROR,
                                errorMessage = error.message ?: "Passenger not found in database."
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        ocrStatus = OcrStatus.ERROR,
                        errorMessage = "An unexpected error occurred: ${e.localizedMessage}"
                    )
                }
            }
        }
    }

    /** Resets the OCR status to IDLE so navigation isn't triggered again on back-stack. */
    fun resetOcrStatus() {
        _state.update { it.copy(ocrStatus = OcrStatus.IDLE) }
    }

    /** Resets error state so the user can retry scanning. */
    fun clearError() {
        _state.update { it.copy(ocrStatus = OcrStatus.IDLE, errorMessage = null) }
    }
}
