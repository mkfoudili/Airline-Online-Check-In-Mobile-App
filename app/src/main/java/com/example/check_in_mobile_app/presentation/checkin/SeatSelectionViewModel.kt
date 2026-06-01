package com.example.check_in_mobile_app.presentation.checkin

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.presentation.components.checkin.SeatModel
import com.example.check_in_mobile_app.presentation.utils.toUserFriendlyMessage
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.SeatRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

data class SeatSelectionUiState(
    val isLoading: Boolean = false,
    val seats: List<SeatModel> = emptyList(),
    val errorMessage: String? = null,
    val isSuccess: Boolean = false
)

@HiltViewModel
class SeatSelectionViewModel @Inject constructor(
    private val seatRepository: SeatRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    var uiState by mutableStateOf(SeatSelectionUiState())
        private set

    fun loadSeats(flightId: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val domainSeats = seatRepository.getSeatMap(flightId)
                val seatModels = domainSeats.map { seat ->
                    SeatModel(
                        seatId = seat.seatId,
                        flightId = seat.flightId,
                        seatNumber = seat.seatNumber,
                        seatClass = seat.seatClass ?: "ECONOMY",
                        isAvailable = seat.isAvailable,
                        isPremium = seat.isPremium,
                        occupiedBy = seat.occupiedBy
                    )
                }
                uiState = uiState.copy(isLoading = false, seats = seatModels)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.toUserFriendlyMessage())
            }
        }
    }

    fun selectSeat(passengerId: String, seatNumber: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            try {
                val uid = authRepository.getCurrentUserId()
                seatRepository.selectSeat(passengerId, seatNumber, uid)
                uiState = uiState.copy(isLoading = false, isSuccess = true)
            } catch (e: Exception) {
                uiState = uiState.copy(isLoading = false, errorMessage = e.toUserFriendlyMessage())
            }
        }
    }
}
