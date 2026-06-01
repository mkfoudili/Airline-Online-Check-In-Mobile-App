package com.example.check_in_mobile_app.presentation.checkin.baggage

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecase.checkin.SelectBaggageUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
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

    fun onCheckedBaggageChange(count: Int) {
        _uiState.update { it.copy(checkedBaggageCount = count) }
    }

    fun onSpecialEquipmentChange(count: Int) {
        _uiState.update { it.copy(specialEquipmentCount = count) }
    }

    fun onContinueClick(onSuccess: () -> Unit) {
        _uiState.update { it.copy(isLoading = true, error = null) }
        
        viewModelScope.launch {
            val result = selectBaggageUseCase(
                passengerId = passengerId,
                checkedBaggageCount = _uiState.value.checkedBaggageCount,
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
