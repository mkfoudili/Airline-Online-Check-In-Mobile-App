package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.booking.GetFlightDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    private val getFlightDetailsUseCase: GetFlightDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingRef: String = checkNotNull(savedStateHandle["bookingRef"])

    private val _uiState = MutableStateFlow<FlightDetailsUiState>(FlightDetailsUiState.Loading)
    val uiState: StateFlow<FlightDetailsUiState> = _uiState

    init {
        fetchDetails()
    }

    private fun fetchDetails() {
        viewModelScope.launch {
            _uiState.value = FlightDetailsUiState.Loading
            
            val result = getFlightDetailsUseCase("user-fatma-001", bookingRef)
            
            result.onSuccess { detailedBooking ->
                _uiState.value = FlightDetailsUiState.Success(detailedBooking)
            }.onFailure { e ->
                _uiState.value = FlightDetailsUiState.Error(e.message ?: "Failed to load flight details")
            }
        }
    }
}
