package com.example.check_in_mobile_app.presentation.main.home

data class HomeUiState(
    val userName: String = "Fatma",
    val flightDestination: String = "London Heathrow (LHR)",
    val isCheckInActive: Boolean = true,
    val bookingReference: String = "",
    val lastName: String = "",
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val errorMessage: String? = null
)