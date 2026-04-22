package com.example.check_in_mobile_app.presentation.main.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.di.NetworkModule
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class HomeViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val networkMonitor = NetworkModule.provideNetworkMonitor(application)

    val isOnline = networkMonitor.isOnline
        .stateIn(viewModelScope, SharingStarted.Eagerly, false)

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
}