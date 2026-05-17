package com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview

import androidx.lifecycle.ViewModel
import com.example.domain.model.Passenger
import com.example.domain.repository.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

data class CheckingDetailsReviewUiState(
    val passenger: Passenger,
    val isConfirmed: Boolean = false
)

@HiltViewModel
class CheckingDetailsReviewViewModel @Inject constructor(
    private val repository: CheckInRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CheckingDetailsReviewUiState(passenger = repository.getPassengerForReview())
    )
    val uiState: StateFlow<CheckingDetailsReviewUiState> = _uiState.asStateFlow()

    fun onConfirmedChanged(confirmed: Boolean) {
        _uiState.update { it.copy(isConfirmed = confirmed) }
    }
}
