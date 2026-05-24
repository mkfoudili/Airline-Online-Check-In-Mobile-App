package com.example.check_in_mobile_app.presentation.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BookingRepository
import com.example.domain.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val bookingRepository: BookingRepository,
    private val flightRepository: FlightRepository,
    private val authRepository: AuthRepository,
    private val networkMonitor: NetworkMonitor,
    private val userPrefs: UserPreferencesRepository
) : AndroidViewModel(application) {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = networkMonitor.currentlyOnline()
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private val _navigateToFlightDetails = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val navigateToFlightDetails = _navigateToFlightDetails.asSharedFlow()

    init {
        loadUserName()
        if (networkMonitor.currentlyOnline()) {
            loadActiveFlight()
        } else {
            loadCachedFlights()
        }
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .drop(1)
                .distinctUntilChanged()
                .collect { online ->
                    if (online) {
                        loadActiveFlight()
                    } else {
                        loadCachedFlights()
                    }
                }
        }
    }

    private fun loadUserName() {
        viewModelScope.launch {
            userPrefs.userNameFlow.firstOrNull()?.let { name ->
                _uiState.update { it.copy(userName = name) }
            }
        }
    }

    fun loadActiveFlight() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUserId() ?: return@launch
            _uiState.update { it.copy(isActiveFlightLoading = true, errorMessage = null) }

            val result = bookingRepository.getUpcomingBookings(uid)
            result
                .onSuccess { bookings ->
                    _uiState.update {
                        it.copy(
                            activeFlight = bookings.firstOrNull(),
                            isActiveFlightLoading = false
                        )
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(
                            isActiveFlightLoading = false,
                            errorMessage = error.message
                        )
                    }
                    loadCachedFlights()
                }
        }
    }

    private fun loadCachedFlights() {
        viewModelScope.launch {
            val flights = flightRepository.getAllCachedFlights()
            _uiState.update { it.copy(cachedFlights = flights) }
        }
    }

    fun onBookingReferenceChange(value: String) {
        _uiState.update { it.copy(bookingReference = value, searchError = null, searchResult = null) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value, searchError = null, searchResult = null) }
    }

    fun clearSearch() {
        _uiState.update { it.copy(searchResult = null, searchError = null) }
    }

    fun onFindFlight() {
        val ref = _uiState.value.bookingReference.trim()
        val lastName = _uiState.value.lastName.trim()

        if (ref.isBlank() || lastName.isBlank()) {
            _uiState.update { it.copy(searchError = SearchError.EmptyFields) }
            return
        }

        viewModelScope.launch {
            _uiState.update {
                it.copy(isSearching = true, searchError = null, searchResult = null)
            }

            val result = bookingRepository.getBooking(ref, lastName)

            result
                .onSuccess { booking ->
                    _uiState.update {
                        it.copy(isSearching = false, searchResult = booking)
                    }
                }
                .onFailure { error ->
                    val searchError = when {
                        error.message == "booking_not_found" -> SearchError.NotFound
                        error is java.net.UnknownHostException ||
                                error is java.net.SocketTimeoutException -> SearchError.NetworkError
                        else -> SearchError.Unknown(error.message ?: "")
                    }
                    _uiState.update {
                        it.copy(isSearching = false, searchError = searchError)
                    }
                }
        }
    }

    fun onCheckInNow() {
        val bookingRef = _uiState.value.activeFlight?.bookingRef?.trim().orEmpty()
        if (bookingRef.isNotBlank()) {
            _navigateToFlightDetails.tryEmit(bookingRef)
        }
    }

    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            loadActiveFlight()
            delay(600)
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}