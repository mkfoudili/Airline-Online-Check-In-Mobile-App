package com.example.check_in_mobile_app.presentation.checkin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Booking
import com.example.domain.usecase.booking.GetFlightDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckInViewModel @Inject constructor(
    private val getFlightDetailsUseCase: GetFlightDetailsUseCase
) : ViewModel() {

    private val _booking = MutableStateFlow<Booking?>(null)
    val booking: StateFlow<Booking?> = _booking.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun loadBooking(bookingRef: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = getFlightDetailsUseCase(bookingRef = bookingRef)
            result.onSuccess {
                _booking.value = it
            }
            _isLoading.value = false
        }
    }
}
