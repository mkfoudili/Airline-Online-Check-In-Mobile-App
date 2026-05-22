package com.example.check_in_mobile_app.presentation.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.model.SpecialRequests
import com.example.domain.repository.CheckInRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecialRequestViewModel @Inject constructor(
    private val checkInRepository: CheckInRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SpecialRequests())
    val uiState: StateFlow<SpecialRequests> = _uiState.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _concludeSuccess = MutableStateFlow(false)
    val concludeSuccess: StateFlow<Boolean> = _concludeSuccess.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init {
        loadPreferences()
    }

    private fun loadPreferences() {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = userPreferencesRepository.userIdFlow.first()
            if (uid != null) {
                checkInRepository.getUserPreferences(uid)
                    .onSuccess { preferences ->
                        _uiState.value = preferences
                    }
                    .onFailure {
                        _error.value = it.message
                    }
            }
            _isLoading.value = false
        }
    }

    fun onToggleSoutien(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(preferredSoutien = enabled)
    }

    fun onToggleVisualsAudit(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(preferredVisualsAudit = enabled)
    }

    fun onToggleChildCare(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(preferredChildCare = enabled)
    }

    fun onTogglePetCare(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(preferredPetCare = enabled)
    }

    fun onToggleMealPreference(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(mealPreference = enabled)
    }

    fun concludeCheckin(passengerId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val uid = userPreferencesRepository.userIdFlow.first()
            if (uid != null) {
                checkInRepository.concludeCheckin(passengerId, uid, _uiState.value)
                    .onSuccess {
                        _concludeSuccess.value = true
                    }
                    .onFailure {
                        _error.value = it.message
                    }
            } else {
                _error.value = "User not logged in"
            }
            _isLoading.value = false
        }
    }
}
