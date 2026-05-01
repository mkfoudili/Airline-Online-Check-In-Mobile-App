package com.example.check_in_mobile_app.presentation.checkin.boarding

import androidx.compose.ui.graphics.ImageBitmap

data class BoardingUiState(
    val passengerName: String = "Djerfi Fatma",
    val passportNumber: String = "AB123456",
    val flightNumber: String = "AH 1042",
    val departureCode: String = "ALG",
    val departureCity: String = "Algiers",
    val arrivalCode: String = "CDG",
    val arrivalCity: String = "Paris",
    val departureDate: String = "April 15, 2026",
    val departureTime: String = "08:35",
    val arrivalTime: String = "11:50",
    val gate: String = "A12",
    val seat: String = "12A",
    val seatClass: String = "Economy",
    val boardingTime: String = "14:20",
    val terminal: String = "T1",
    val baggageAllowance: String = "23 kg",
    val bookingReference: String = "SK7Y29",
    val flightStatus: FlightStatus = FlightStatus.BOARDING,
    val qrCodeData: String = "BOARDING:AH1042:SK7Y29:14A:ALG-CDG",
    val qrBitmap: ImageBitmap? = null,
    val isLoading: Boolean = true,
    val isDownloadingPdf: Boolean = false,
    val isOffline: Boolean = false,
    val isPdfGenerated: Boolean = false,
    val errorMessage: String? = null,
    val showDownloadSuccess: Boolean = false
)

enum class FlightStatus { OPEN, BOARDING, CLOSED, DEPARTED }