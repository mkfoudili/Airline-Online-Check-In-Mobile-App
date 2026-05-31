package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BoardingPassRepository
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
    private val boardingPassRepository: BoardingPassRepository,
    private val authRepository: AuthRepository,
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
        if (networkMonitor.currentlyOnline()) {
            fetchBookings()
        } else {
            loadFromCache()
        }
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .drop(1)
                .distinctUntilChanged()
                .collect { online ->
                    if (online) fetchBookings() else loadFromCache()
                }
        }
    }

    private fun fetchBookings() {
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            val result = getUpcomingBookingsUseCase()
            result.onSuccess { bookings ->
                _uiState.value = BookingUiState.Success(bookings)
            }.onFailure {
                loadFromCache()
            }
        }
    }

    /**
     * Offline : reconstruit l'historique des check-ins à partir des boarding passes
     * du user connecté uniquement. Chaque boarding pass = un check-in effectué.
     */
    private fun loadFromCache() {
        viewModelScope.launch {
            val uid = authRepository.getCurrentUserId()
            if (uid == null) {
                _uiState.value = BookingUiState.Success(emptyList())
                return@launch
            }

            val boardingPasses = boardingPassRepository.getBoardingPassesByUid(uid)
            val offlineBookings = boardingPasses.map { bp ->
                Booking(
                    bookingId  = bp.flightId,
                    bookingRef = bp.bookingReference,
                    pnr        = bp.bookingReference,
                    lastName   = "",
                    status     = CheckInStatus.CHECKED_IN,
                    checkinPassengerId  = bp.passengerId,
                    flight     = Flight(
                        flightId         = bp.flightId,
                        flightNumber     = bp.flightNumber,
                        origin           = bp.origin,
                        originCity       = bp.originCity,
                        destination      = bp.destination,
                        destinationCity  = bp.destinationCity,
                        departureTime    = bp.departureTime ?: 0L,
                        arrivalTime      = bp.arrivalTime ?: 0L,
                        gate             = bp.gate ?: "",
                        terminal         = bp.terminal ?: "",
                        boardingTime     = bp.boardingTime ?: "",
                        checkInOpensTime = "",
                        aircraftType     = null,
                        status           = null
                    ),
                    passengers = emptyList()
                )
            }
            _uiState.value = BookingUiState.Success(offlineBookings)
        }
    }
}