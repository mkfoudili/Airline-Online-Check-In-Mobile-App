package com.example.check_in_mobile_app.presentation.checkin.baggage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.checkin.SelectBaggageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BaggageViewModel @Inject constructor(
    private val selectBaggageUseCase: SelectBaggageUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val passengerId: String = checkNotNull(savedStateHandle["passengerId"])

    private val _uiState = MutableStateFlow(BaggageUiState())
    val uiState: StateFlow<BaggageUiState> = _uiState.asStateFlow()

    // Track the running API job so we can cancel it instantly on back
    private var continueJob: Job? = null

    fun onCheckedBaggageChange(count: Int) {
        _uiState.update { it.copy(checkedBaggageCount = count) }
    }

    fun onSpecialEquipmentChange(count: Int) {
        _uiState.update { it.copy(specialEquipmentCount = count) }
    }

    fun onBackClick() {
        // Cancel any in-flight API call immediately so back is instant
        continueJob?.cancel()
        continueJob = null
        _uiState.update { it.copy(isLoading = false, error = null) }
    }

    fun onContinueClick(onSuccess: () -> Unit) {
        // Prevent double-tap
        if (_uiState.value.isLoading) return

        _uiState.update { it.copy(isLoading = true, error = null) }

        continueJob = viewModelScope.launch {
            val result = selectBaggageUseCase(
                passengerId           = passengerId,
                checkedBaggageCount   = _uiState.value.checkedBaggageCount,
                specialEquipmentCount = _uiState.value.specialEquipmentCount
            )

            _uiState.update { it.copy(isLoading = false) }

            result.onSuccess {
                onSuccess()
            }.onFailure { throwable ->
                _uiState.update { it.copy(error = throwable.message) }
            }
        }
    }
}