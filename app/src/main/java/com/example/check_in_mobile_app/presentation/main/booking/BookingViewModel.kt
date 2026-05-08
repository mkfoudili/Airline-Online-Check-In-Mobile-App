package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<BookingUiState>(
        BookingUiState.Success(getUpcomingBookingsUseCase())
    )
    val uiState: StateFlow<BookingUiState> = _uiState
}

