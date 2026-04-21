package com.example.check_in_mobile_app.presentation.main.booking

import com.example.domain.model.Booking

sealed class BookingUiState {
    object Loading : BookingUiState()
    data class Success(val bookings: List<Booking>) : BookingUiState()
    data class Error(val message: String) : BookingUiState()
}
