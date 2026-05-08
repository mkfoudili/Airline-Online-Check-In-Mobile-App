package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import com.example.domain.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val flightRepository: FlightRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingRef: String = checkNotNull(savedStateHandle["bookingRef"])

    private val _booking = MutableStateFlow<Booking?>(null)
    val booking: StateFlow<Booking?> = _booking

    init {
        fetchBookingAndFlightDetails()
    }

    private fun fetchBookingAndFlightDetails() {
        viewModelScope.launch {
            // 1. Get the booking to find the flightId and passenger details
            val bookingResult = bookingRepository.getUpcomingBookings("user-fatma-001")
            bookingResult.onSuccess { bookings ->
                val foundBooking = bookings.find { it.bookingRef == bookingRef }
                if (foundBooking != null) {
                    // 2. Extract flightId and fetch fresh details from the dedicated flight service
                    val flightResult = flightRepository.getFlightById(foundBooking.flight.flightId)
                    flightResult.onSuccess { freshFlight ->
                        // 3. Update the booking with the latest flight details
                        _booking.value = foundBooking.copy(flight = freshFlight)
                    }.onFailure {
                        // Fallback to the flight info already in the booking if flight service fails
                        _booking.value = foundBooking
                    }
                }
            }
        }
    }
}
