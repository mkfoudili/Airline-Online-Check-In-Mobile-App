package com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview

import androidx.lifecycle.ViewModel
import com.example.data.repository.CheckInRepositoryImpl
import com.example.domain.model.Passenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class CheckingDetailsReviewUiState(
    val passenger: Passenger,
    val isConfirmed: Boolean = false
)

class CheckingDetailsReviewViewModel : ViewModel() {

    private val repository = CheckInRepositoryImpl()

    private val _uiState = MutableStateFlow(
        CheckingDetailsReviewUiState(passenger = repository.getPassengerForReview())
    )
    val uiState: StateFlow<CheckingDetailsReviewUiState> = _uiState.asStateFlow()

    fun onConfirmedChanged(confirmed: Boolean) {
        _uiState.update { it.copy(isConfirmed = confirmed) }
    }
}
