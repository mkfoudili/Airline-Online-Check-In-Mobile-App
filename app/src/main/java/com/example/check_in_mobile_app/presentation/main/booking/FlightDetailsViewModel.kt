package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.model.Booking
import com.example.domain.repository.FlightRepository
import com.example.domain.usecase.booking.GetFlightDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    private val getFlightDetailsUseCase: GetFlightDetailsUseCase,
    private val flightRepository: FlightRepository,
    private val networkMonitor: NetworkMonitor,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingRef: String = checkNotNull(savedStateHandle["bookingRef"])

    private val _uiState = MutableStateFlow<FlightDetailsUiState>(FlightDetailsUiState.Loading)
    val uiState: StateFlow<FlightDetailsUiState> = _uiState

    init {
        fetchDetails(isOnline = networkMonitor.currentlyOnline())
        observeConnectivity()
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            networkMonitor.isOnline
                .drop(1)
                .distinctUntilChanged()
                .collect { online ->
                    if (online) {
                        // Retour online : recharger sans passer par Loading
                        // pour éviter le flash d'écran vide / gris
                        fetchDetails(isOnline = true, showLoading = false)
                    } else {
                        // Passage offline : on garde les données affichées,
                        // on met juste isOnline = false pour mettre à jour l'UI
                        val current = _uiState.value
                        if (current is FlightDetailsUiState.Success) {
                            _uiState.value = current.copy(isOnline = false)
                        } else {
                            // Pas encore de Success → charger depuis le cache
                            loadFromCache()
                        }
                    }
                }
        }
    }

    private fun fetchDetails(isOnline: Boolean, showLoading: Boolean = true) {
        viewModelScope.launch {
            if (showLoading) {
                _uiState.value = FlightDetailsUiState.Loading
            }

            if (isOnline) {
                val result = getFlightDetailsUseCase(bookingRef = bookingRef)
                result
                    .onSuccess { booking ->
                        _uiState.value = FlightDetailsUiState.Success(booking, isOnline = true)
                    }
                    .onFailure {
                        // Réseau KO malgré isOnline → fallback cache silencieux
                        // (pas d'Error si le cache répond)
                        loadFromCache()
                    }
            } else {
                loadFromCache()
            }
        }
    }

    /**
     * Charge depuis le cache local.
     * N'écrit jamais Error si on a déjà un Success affiché.
     */
    private suspend fun loadFromCache() {
        val cachedFlights = flightRepository.getAllCachedFlights()
        val flight = cachedFlights.firstOrNull { it.flightId == bookingRef }

        if (flight != null) {
            val offlineBooking = Booking(
                bookingId  = bookingRef,
                bookingRef = bookingRef,
                pnr        = bookingRef,
                lastName   = "",
                status     = com.example.domain.model.CheckInStatus.CONFIRMED,
                flight     = flight,
                passengers = emptyList()
            )
            _uiState.value = FlightDetailsUiState.Success(offlineBooking, isOnline = false)
        } else {
            // N'écraser un Success existant par une Error que si on n'a vraiment rien
            if (_uiState.value !is FlightDetailsUiState.Success) {
                _uiState.value = FlightDetailsUiState.Error("Aucun vol en cache pour cette référence")
            }
        }
    }
}