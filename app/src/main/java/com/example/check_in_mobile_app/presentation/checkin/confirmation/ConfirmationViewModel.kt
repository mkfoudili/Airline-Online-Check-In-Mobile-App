package com.example.check_in_mobile_app.presentation.checkin.confirmation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.pdf.PdfGenerator
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.repository.CheckInRepository
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ConfirmationViewModel @Inject constructor(
    private val boardingPassRepository: BoardingPassRepository,
    private val generatePdfUseCase: GeneratePdfUseCase,
    private val checkInRepository: CheckInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    private var currentBoardingPass: BoardingPass? = null

    fun generateBoardingPass(passengerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, errorMessage = null) }
            try {
                withContext(Dispatchers.IO) {
                    // Avancer la session au dernier step requis
                    checkInRepository.advanceSessionStep(
                        passengerId = passengerId,
                        step        = "SPECIAL_REQUESTS"
                    )
                    // Générer le boarding pass
                    boardingPassRepository.generateAndSyncFromServer(passengerId)
                }.let { boardingPass ->
                    currentBoardingPass = boardingPass
                    _uiState.update {
                        it.copy(
                            isGenerating = false,
                            boardingPass = boardingPass
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        errorMessage = e.message
                    )
                }
            }
        }
    }

    fun onDownloadPdf(context: Context) {
        val pass = currentBoardingPass ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloadingPdf = true) }
            val pdfData = generatePdfUseCase(pass)
            val result = withContext(Dispatchers.IO) {
                PdfGenerator.generate(context, pdfData)
            }
            result.onSuccess { pdfResult ->
                _uiState.update {
                    it.copy(isDownloadingPdf = false, showDownloadSuccess = true)
                }
                PdfGenerator.openWithChooser(context, pdfResult.uri)
            }.onFailure { error ->
                _uiState.update {
                    it.copy(isDownloadingPdf = false, errorMessage = error.message)
                }
            }
        }
    }

    fun onDismissSuccess() {
        _uiState.update { it.copy(showDownloadSuccess = false) }
    }
}