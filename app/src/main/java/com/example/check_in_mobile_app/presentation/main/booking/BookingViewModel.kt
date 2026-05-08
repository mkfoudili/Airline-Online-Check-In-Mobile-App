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
            val bookings = getUpcomingBookingsUseCase("user-fatma-001")
            _uiState.value = BookingUiState.Success(bookings)
        }
    }
}

