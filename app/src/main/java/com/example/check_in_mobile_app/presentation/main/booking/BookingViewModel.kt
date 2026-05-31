package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import com.example.check_in_mobile_app.data.NotificationManager
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.Passenger
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
    private val networkMonitor: NetworkMonitor,
    notificationManager: NotificationManager
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = networkMonitor.currentlyOnline()
        )

    val hasUnread: StateFlow<Boolean> = notificationManager.hasUnread

    private val _uiState = MutableStateFlow<BookingUiState>(BookingUiState.Loading)
    val uiState: StateFlow<BookingUiState> = _uiState

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

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

    /** Appelé par le pull-to-refresh de l'UI */
    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            if (networkMonitor.currentlyOnline()) {
                val uid = authRepository.getCurrentUserId()
                if (uid != null) {
                    boardingPassRepository.refreshBoardingPassesFromRemote(uid)
                }
            }
            val uid = authRepository.getCurrentUserId()
            loadFromCacheInternal(uid)
            _isRefreshing.value = false
        }
    }

    private fun fetchBookings() {
        viewModelScope.launch {
            _uiState.value = BookingUiState.Loading
            val uid = authRepository.getCurrentUserId()
            if (uid == null) {
                _uiState.value = BookingUiState.Success(emptyList())
                return@launch
            }
            // Sync boarding passes depuis le serveur, puis reconstruire
            // exactement comme en offline : 1 carte par passager checké.
            boardingPassRepository.refreshBoardingPassesFromRemote(uid)
                .onFailure { /* on continue avec le cache existant */ }
            loadFromCacheInternal(uid)
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
            loadFromCacheInternal(uid)
        }
    }

    private suspend fun loadFromCacheInternal(uid: String? = null) {
        val resolvedUid = uid ?: authRepository.getCurrentUserId()
        if (resolvedUid == null) {
            _uiState.value = BookingUiState.Success(emptyList())
            return
        }

        val boardingPasses = boardingPassRepository.getBoardingPassesByUid(resolvedUid)
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
                passengers = run {
                    val parts = bp.passengerName.trim().split(" ", limit = 2)
                    listOf(
                        Passenger(
                            passengerId    = bp.passengerId,
                            bookingId      = bp.flightId,
                            uid            = null,
                            firstName      = parts.getOrElse(0) { "" },
                            lastName       = parts.getOrElse(1) { "" },
                            passportNumber = null,
                            nationality    = null,
                            dateOfBirth    = null,
                            expiryDate     = null,
                            seatNumber     = bp.seatNumber,
                            checkinStatus  = null
                        )
                    )
                }
            )
        }
        _uiState.value = BookingUiState.Success(offlineBookings)
    }
}