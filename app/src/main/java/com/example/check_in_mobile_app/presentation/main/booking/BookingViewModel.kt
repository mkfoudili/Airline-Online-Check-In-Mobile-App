package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.check_in_mobile_app.di.AppContainer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class BookingViewModel : ViewModel() {

    private val useCase = AppContainer.getUpcomingBookingsUseCase

    private val _uiState = MutableStateFlow<BookingUiState>(
        BookingUiState.Success(useCase())
    )
    val uiState: StateFlow<BookingUiState> = _uiState
}

