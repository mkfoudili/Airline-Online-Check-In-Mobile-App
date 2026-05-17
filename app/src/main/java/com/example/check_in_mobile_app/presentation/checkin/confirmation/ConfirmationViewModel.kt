package com.example.check_in_mobile_app.presentation.checkin.confirmation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.pdf.PdfGenerator
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
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
    private val generatePdfUseCase: GeneratePdfUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    // Boarding pass kept in memory once generated — used for PDF generation
    private var currentBoardingPass: BoardingPass? = null

    /**
     * Called when the user confirms step 5 (Special Requests).
     * Generates the boarding pass server-side and caches it locally.
     *
     * [passengerId] must be the real passenger ID coming from the check-in session.
     */
    fun generateBoardingPass(passengerId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isGenerating = true, errorMessage = null) }
            try {
                val boardingPass = withContext(Dispatchers.IO) {
                    boardingPassRepository.generateAndSyncFromServer(passengerId)
                }
                currentBoardingPass = boardingPass
                _uiState.update {
                    it.copy(
                        isGenerating = false,
                        boardingPass = boardingPass
                    )
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

    /**
     * Downloads the boarding pass as a PDF and triggers the "Open with" chooser
     * so the user can open it with Drive, a PDF viewer, etc.
     */
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
                // Launch the "Open with" chooser (Drive, PDF viewer, etc.)
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