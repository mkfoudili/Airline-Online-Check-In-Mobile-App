package com.example.check_in_mobile_app.presentation.home

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onBookingReferenceChange(value: String) {
        _uiState.update { it.copy(bookingReference = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
    }

    fun onFindFlight() {
        // TODO: implement flight search logic
    }

    fun onCheckInNow() {
        // TODO: navigate to check-in flow
    }
}