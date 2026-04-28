package com.example.check_in_mobile_app.presentation.checkin.boarding

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.data.pdf.PdfGenerator
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BoardingViewModel(
    private val boardingPassRepository: BoardingPassRepository,
    private val generateQRCodeUseCase: GenerateQRCodeUseCase,
    private val generatePdfUseCase: GeneratePdfUseCase,
    // Default passengerId targets the first mock passenger
    private val passengerId: String = "p1"
) : ViewModel() {

    private val _uiState = MutableStateFlow(BoardingUiState())
    val uiState: StateFlow<BoardingUiState> = _uiState.asStateFlow()

    init {
        loadBoardingPass()
    }

    private fun loadBoardingPass() {
        viewModelScope.launch {
            // Collect the Flow emitted by the DAO (Room emits on every DB change)
            boardingPassRepository.getBoardingPass(passengerId).collect { boardingPass ->
                if (boardingPass != null) {
                    val qrData = generateQRCodeUseCase(boardingPass)
                    _uiState.update { current ->
                        current.copy(
                            passengerName = boardingPass.passengerName,
                            flightNumber = boardingPass.flightNumber,
                            departureCode = boardingPass.origin,
                            departureCity = boardingPass.originCity,
                            arrivalCode = boardingPass.destination,
                            arrivalCity = boardingPass.destinationCity,
                            gate = boardingPass.gate ?: "TBD",
                            seat = boardingPass.seatNumber ?: "N/A",
                            boardingTime = boardingPass.boardingTime ?: "N/A",
                            terminal = boardingPass.terminal ?: "N/A",
                            bookingReference = boardingPass.bookingReference,
                            qrCodeData = qrData,
                            isOffline = !boardingPass.isSyncedWithServer,
                            isLoading = false
                        )
                    }
                } else {
                    // No local data; show loading indicator until mock seeds
                    _uiState.update { it.copy(isLoading = true) }
                }
            }
        }
    }

    fun onDownloadPdf(context: Context) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Reload pass to build the PDF data
            val pass = boardingPassRepository
                .getBoardingPass(passengerId)
                .let { flow ->
                    var result = com.example.domain.model.BoardingPass(
                        passId = "BP-001",
                        passengerId = passengerId,
                        flightId = "f1",
                        flightNumber = _uiState.value.flightNumber,
                        origin = _uiState.value.departureCode,
                        originCity = _uiState.value.departureCity,
                        destination = _uiState.value.arrivalCode,
                        destinationCity = _uiState.value.arrivalCity,
                        passengerName = _uiState.value.passengerName,
                        seatNumber = _uiState.value.seat,
                        gate = _uiState.value.gate,
                        boardingTime = _uiState.value.boardingTime,
                        departureTime = System.currentTimeMillis() + 86_400_000L,
                        arrivalTime = System.currentTimeMillis() + 90_000_000L,
                        bookingReference = _uiState.value.bookingReference,
                        terminal = _uiState.value.terminal,
                        qrCodeData = _uiState.value.qrCodeData,
                        issuedAt = System.currentTimeMillis()
                    )
                    result
                }

            val pdfData = generatePdfUseCase(pass)
            val result = PdfGenerator.generate(context, pdfData)

            _uiState.update {
                it.copy(
                    isLoading = false,
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

class BoardingViewModelFactory(
    private val boardingPassRepository: BoardingPassRepository,
    private val generateQRCodeUseCase: GenerateQRCodeUseCase,
    private val generatePdfUseCase: GeneratePdfUseCase,
    private val passengerId: String = "p1"
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BoardingViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BoardingViewModel(
                boardingPassRepository,
                generateQRCodeUseCase,
                generatePdfUseCase,
                passengerId
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
    }
}