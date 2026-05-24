package com.example.check_in_mobile_app.presentation.main.booking

import com.example.domain.model.Booking

sealed class FlightDetailsUiState {
    object Loading : FlightDetailsUiState()
    data class Success(val booking: Booking, val isOnline: Boolean) : FlightDetailsUiState()
    data class Error(val message: String) : FlightDetailsUiState()
}