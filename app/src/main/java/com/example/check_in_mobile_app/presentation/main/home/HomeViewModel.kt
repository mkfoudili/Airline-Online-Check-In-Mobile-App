package com.example.check_in_mobile_app.presentation.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    application: Application,
    private val bookingRepository: BookingRepository,
    private val authRepository: AuthRepository,
    private val networkMonitor: NetworkMonitor
) : AndroidViewModel(application) {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = networkMonitor.currentlyOnline()
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadUserName()
        loadActiveFlight()
    }

    private fun loadUserName() {
        val uid = authRepository.getCurrentUserId() ?: return
        // Le nom complet n'est pas stocké directement dans AuthRepository ;
        // on utilise l'UID comme fallback (à enrichir si ProfileRepository est ajouté)
        _uiState.update { it.copy(userName = uid.take(12)) }
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
                }
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
        // Navigue vers le check-in pour le vol actif, géré par le NavGraph
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