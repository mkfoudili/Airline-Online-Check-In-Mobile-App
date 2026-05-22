package com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.Passenger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CheckingDetailsReviewUiState(
    val passenger: Passenger? = null,
    val isConfirmed: Boolean = false
)

@HiltViewModel
class CheckingDetailsReviewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(CheckingDetailsReviewUiState())
    val uiState: StateFlow<CheckingDetailsReviewUiState> = _uiState.asStateFlow()

    fun setPassenger(passenger: Passenger) {
        _uiState.update { it.copy(passenger = passenger) }
    }

    fun onConfirmedChanged(confirmed: Boolean) {
        _uiState.update { it.copy(isConfirmed = confirmed) }
    }
}