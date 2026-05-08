package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState

    init {
        fetchBookings()
    }

    private fun fetchBookings() {
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            val result = getUpcomingBookingsUseCase()
            
            result.onSuccess { bookings ->
                _uiState.value = BookingUiState.Success(bookings)
            }.onFailure { error ->
                _uiState.value = BookingUiState.Error(error.message ?: "An unexpected error occurred")
            }
        }
    }
}
