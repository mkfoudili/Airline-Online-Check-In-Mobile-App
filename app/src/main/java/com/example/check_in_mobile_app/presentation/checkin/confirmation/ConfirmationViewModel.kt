package com.example.check_in_mobile_app.presentation.checkin.confirmation

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.pdf.PdfGenerator
import com.example.domain.model.BoardingPass
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ConfirmationViewModel(
    private val generatePdfUseCase: GeneratePdfUseCase = GeneratePdfUseCase()
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmationUiState())
    val uiState: StateFlow<ConfirmationUiState> = _uiState.asStateFlow()

    fun onDownloadPdf(context: Context, boardingPass: BoardingPass) {
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloadingPdf = true) }
            val pdfData = generatePdfUseCase(boardingPass)
            val result = withContext(Dispatchers.IO) {
                PdfGenerator.generate(context, pdfData)
            }
            _uiState.update {
                it.copy(
                    isDownloadingPdf = false,
                    showDownloadSuccess = result.isSuccess,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun onDismissSuccess() {
        _uiState.update { it.copy(showDownloadSuccess = false) }
    }
}