package com.example.check_in_mobile_app.presentation.checkin.boarding

import android.content.Context
import androidx.compose.ui.graphics.asImageBitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.mapper.toBitmap
import com.example.data.pdf.PdfGenerator
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeBitmapUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class BoardingViewModel @Inject constructor(
    private val boardingPassRepository: BoardingPassRepository,
    private val generateQRCodeUseCase: GenerateQRCodeUseCase,
    private val generateQRCodeBitmapUseCase: GenerateQRCodeBitmapUseCase,
    private val generatePdfUseCase: GeneratePdfUseCase,
) : ViewModel() {

    private val passengerId: String = "p1"

    private val _uiState = MutableStateFlow(BoardingUiState(isLoading = true))
    val uiState: StateFlow<BoardingUiState> = _uiState.asStateFlow()

    private var currentPass: BoardingPass? = null
    private var cachedQrPayload: String? = null

    init {
        viewModelScope.launch { boardingPassRepository.seedMockDataIfEmpty() }
        loadBoardingPass()
    }

    private fun loadBoardingPass() {
        viewModelScope.launch {
            boardingPassRepository.getBoardingPass(passengerId).collect { boardingPass ->
                if (boardingPass == null) {
                    _uiState.update { it.copy(isLoading = false, isEmpty = true) }
                    return@collect
                }
                currentPass = boardingPass
                val qrPayload = generateQRCodeUseCase(boardingPass)
                val qrBitmap = if (qrPayload == cachedQrPayload && _uiState.value.qrBitmap != null) {
                    _uiState.value.qrBitmap
                } else {
                    cachedQrPayload = qrPayload
                    withContext(Dispatchers.Default) {
                        generateQRCodeBitmapUseCase(qrPayload)?.toBitmap()?.asImageBitmap()
                    }
                }
                _uiState.update { current ->
                    current.copy(
                        passengerName = boardingPass.passengerName,
                        flightNumber = boardingPass.flightNumber,
                        departureCode = boardingPass.origin,
                        departureCity = boardingPass.originCity,
                        arrivalCode = boardingPass.destination,
                        arrivalCity = boardingPass.destinationCity,
                        gate = boardingPass.gate ?: current.gate,
                        seat = boardingPass.seatNumber ?: current.seat,
                        boardingTime = boardingPass.boardingTime ?: current.boardingTime,
                        terminal = boardingPass.terminal ?: current.terminal,
                        bookingReference = boardingPass.bookingReference,
                        qrCodeData = boardingPass.qrCodeData ?: current.qrCodeData,
                        qrBitmap = qrBitmap,
                        isOffline = !boardingPass.isSyncedWithServer,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onDownloadPdf(context: Context) {
        val pass = currentPass ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isDownloadingPdf = true) }
            val pdfData = generatePdfUseCase(pass)
            val result = withContext(Dispatchers.IO) {
                PdfGenerator.generate(context, pdfData)
            }
            _uiState.update {
                it.copy(
                    isDownloadingPdf = false,
                    isPdfGenerated = result.isSuccess,
                    showDownloadSuccess = result.isSuccess,
                    errorMessage = result.exceptionOrNull()?.message
                )
            }
        }
    }

    fun onDismissDownloadSuccess() {
        _uiState.update { it.copy(showDownloadSuccess = false) }
    }

    fun onConnectivityChanged(isConnected: Boolean) {
        _uiState.update { it.copy(isOffline = !isConnected) }
    }
}