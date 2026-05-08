package com.example.check_in_mobile_app.presentation.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.BaseApplication
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.example.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val bookingRepository: BookingRepository
) : AndroidViewModel(application) {

    private val networkMonitor = (application as BaseApplication).networkMonitor

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = networkMonitor.currentlyOnline()
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUpcomingFlight()
    }

    private fun loadUpcomingFlight() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = bookingRepository.getUpcomingBookings("user-fatma-001")
            result.onSuccess { bookings ->
                if (bookings.isNotEmpty()) {
                    val nextFlight = bookings.first().flight
                    _uiState.update { it.copy(
                        flightDestination = "${nextFlight.destinationCity} (${nextFlight.destination})",
                        isLoading = false
                    ) }
                } else {
                    _uiState.update { it.copy(isLoading = false, flightDestination = "No upcoming flights") }
                }
            }.onFailure { error ->
                _uiState.update { it.copy(isLoading = false, errorMessage = error.message) }
            }
        }
    }

    fun onBookingReferenceChange(value: String) {
        _uiState.update { it.copy(bookingReference = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
    }

    fun onFindFlight() { /* TODO */ }

    fun onCheckInNow() { /* TODO */ }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadUpcomingFlight()
            delay(800)
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}