package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

@HiltViewModel
class BookingViewModel @Inject constructor(
    private val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = networkMonitor.currentlyOnline()
        )

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState

    init {
        fetchBookings()
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .drop(1)
                .distinctUntilChanged()
                .collect { online ->
                    if (online) {
                        fetchBookings()
                    }
                }
        }
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
