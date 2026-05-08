package com.example.check_in_mobile_app.presentation.main.booking

import com.example.domain.model.Booking

sealed class FlightDetailsUiState {
    object Loading : FlightDetailsUiState()
    data class Success(val booking: Booking) : FlightDetailsUiState()
    data class Error(val message: String) : FlightDetailsUiState()
}
