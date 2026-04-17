package com.example.check_in_mobile_app.presentation.booking

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookingViewModel : ViewModel() {

    private val useCase = GetUpcomingBookingsUseCase(com.example.data.repository.BookingRepositoryImpl())

    private val _uiState = MutableStateFlow<BookingUiState>(
        BookingUiState.Success(useCase())
    )
    val uiState: StateFlow<BookingUiState> = _uiState
}
