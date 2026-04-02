package com.example.check_in_mobile_app.presentation.booking

import androidx.lifecycle.ViewModel
import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookingViewModel : ViewModel() {

    private val useCase = GetUpcomingBookingsUseCase(BookingRepositoryImpl())

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState

    init {
        loadBookings()
    }

    private fun loadBookings() {
        val bookings = useCase()
        _uiState.value = BookingUiState.Success(bookings)
    }
}
