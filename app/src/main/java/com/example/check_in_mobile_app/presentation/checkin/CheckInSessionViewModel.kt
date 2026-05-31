package com.example.check_in_mobile_app.presentation.checkin

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.repository.CheckInRepository
import com.example.domain.usecase.checkin.VerifyPassportUseCase
import com.example.domain.usecase.ocr.ExtractPassportDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

enum class OcrStatus { IDLE, SCANNING, VERIFYING, CREATING_SESSION, SUCCESS, ERROR }

data class CheckInSessionState(
    val verifiedPassenger: Passenger? = null,
    val activeSession: CheckInSession? = null,
    val ocrStatus: OcrStatus = OcrStatus.IDLE,
    val errorMessage: String? = null
)

@HiltViewModel
class CheckInSessionViewModel @Inject constructor(
    private val extractPassportDataUseCase: ExtractPassportDataUseCase,
    private val verifyPassportUseCase: VerifyPassportUseCase,
    private val checkInRepository: CheckInRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CheckInSessionState())
    val state: StateFlow<CheckInSessionState> = _state.asStateFlow()

    fun startOcrAndVerify(bitmap: Bitmap, bookingId: String) {
        viewModelScope.launch {
            try {
                // Étape 1 : OCR
                _state.update { it.copy(ocrStatus = OcrStatus.SCANNING, errorMessage = null) }

                val parsed = extractPassportDataUseCase(bitmap)
                val ocrText = parsed?.rawText ?: ""
                println("OCR_DEBUG: Full raw text: $ocrText")

                val extractedPassportNumber = parsed?.passportNumber ?: ""
                val extractedLastName = parsed?.lastName ?: ""

                if (extractedPassportNumber.isBlank() || extractedLastName.isBlank()) {
                    _state.update {
                        it.copy(
                            ocrStatus    = OcrStatus.ERROR,
                            errorMessage = "We couldn't read your passport details clearly. Please place your passport on a flat surface in a well-lit area (free of glare or shadows), and make sure the camera is focused."
                        )
                    }
                    return@launch
                }

                // Étape 2 : Vérification backend
                _state.update { it.copy(ocrStatus = OcrStatus.VERIFYING) }

                val verifyResult = verifyPassportUseCase(
                    passportNumber = extractedPassportNumber,
                    lastName       = extractedLastName,
                    firstName      = parsed?.firstName,
                    nationality    = parsed?.nationality,
                    dateOfBirth    = parsed?.dateOfBirth,
                    expiryDate     = parsed?.expiryDate
                )

                val passenger = verifyResult.getOrElse { error ->
                    val userFriendlyError = when {
                        error.message?.contains("does not match any booking", ignoreCase = true) == true ||
                        error.message?.contains("Passenger not found", ignoreCase = true) == true -> {
                            "We couldn't find a matching passenger for these passport details. Please make sure the passport belongs to the passenger on this booking, and try scanning again in good lighting."
                        }
                        error.message?.contains("Network error", ignoreCase = true) == true ||
                        error.message?.contains("connection", ignoreCase = true) == true -> {
                            "Connection issue. Please check your network and try again."
                        }
                        else -> error.message ?: "Passenger not found in database."
                    }
                    _state.update {
                        it.copy(
                            ocrStatus    = OcrStatus.ERROR,
                            errorMessage = userFriendlyError
                        )
                    }
                    return@launch
                }

                if (passenger.checkinStatus == "CHECKED_IN") {
                    _state.update {
                        it.copy(
                            ocrStatus    = OcrStatus.ERROR,
                            errorMessage = "This passenger has already checked in."
                        )
                    }
                    return@launch
                }

                // Étape 3 : Création/reprise de la session
                _state.update { it.copy(ocrStatus = OcrStatus.CREATING_SESSION) }

                val sessionResult = checkInRepository.createOrResumeSession(
                    passengerId = passenger.passengerId,
                    bookingId   = passenger.bookingId
                )

                sessionResult.fold(
                    onSuccess = { session ->
                        _state.update {
                            it.copy(
                                ocrStatus         = OcrStatus.SUCCESS,
                                verifiedPassenger = passenger,
                                activeSession     = session,
                                errorMessage      = null
                            )
                        }
                    },
                    onFailure = { error ->
                        _state.update {
                            it.copy(
                                ocrStatus    = OcrStatus.ERROR,
                                errorMessage = error.message ?: "Failed to start check-in session."
                            )
                        }
                    }
                )
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        ocrStatus    = OcrStatus.ERROR,
                        errorMessage = "Something went wrong while scanning. Please capture your passport again in a well-lit room or upload a clear photo from your gallery."
                    )
                }
            }
        }
    }

    fun resetOcrStatus() {
        _state.update { it.copy(ocrStatus = OcrStatus.IDLE) }
    }

    fun clearError() {
        _state.update { it.copy(ocrStatus = OcrStatus.IDLE, errorMessage = null) }
    }
}
