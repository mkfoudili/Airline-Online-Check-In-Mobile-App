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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Use the singleton monitor from BaseApplication so there is only
    // one NetworkCallback registered for the whole app lifetime.
    private val networkMonitor = (application as BaseApplication).networkMonitor

    // WhileSubscribed(5_000) keeps the network callback alive for 5 s after
    // the last subscriber leaves (e.g. screen rotation) to avoid flicker.
    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = networkMonitor.currentlyOnline()   // <-- read initial value synchronously
        )

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun onBookingReferenceChange(value: String) {
        _uiState.update { it.copy(bookingReference = value) }
    }

    fun onLastNameChange(value: String) {
        _uiState.update { it.copy(lastName = value) }
    }

    fun onFindFlight() { /* TODO */ }

    fun onCheckInNow() { /* TODO */ }

    /**
     * Pull-to-refresh: re-check connectivity and simulate a data reload.
     * The NetworkMonitor Flow already re-emits automatically on state change;
     * this gives the user visual feedback and a manual re-trigger.
     */
    fun onRefresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(isRefreshing = true) }
            // Re-check connectivity immediately by reading the current state.
            // The NetworkMonitor callback handles the automatic detection;
            // this is just a minimum delay for the spinner to be visible.
            delay(800)
            _uiState.update { it.copy(isRefreshing = false) }
        }
    }
}