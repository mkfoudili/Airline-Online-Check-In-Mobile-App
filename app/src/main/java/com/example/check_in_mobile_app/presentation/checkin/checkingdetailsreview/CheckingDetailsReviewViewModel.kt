package com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview

import androidx.lifecycle.ViewModel
import com.example.domain.model.Passenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CheckingDetailsReviewUiState(
    val passenger: Passenger,
    val isConfirmed: Boolean = false
)

/**
 * Receives the already-verified [Passenger] from [CheckInSessionViewModel]
 * (passed in via the nav graph). No more hardcoded data.
 */
class CheckingDetailsReviewViewModel(
    verifiedPassenger: Passenger
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CheckingDetailsReviewUiState(passenger = verifiedPassenger)
    )
    val uiState: StateFlow<CheckingDetailsReviewUiState> = _uiState.asStateFlow()

    fun onConfirmedChanged(confirmed: Boolean) {
        _uiState.update { it.copy(isConfirmed = confirmed) }
    }
}
