package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FlightDetailsViewModel @Inject constructor(
    private val bookingRepository: BookingRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val bookingRef: String = checkNotNull(savedStateHandle["bookingRef"])

    val booking: Booking? = bookingRepository.getUpcomingBookings().find { 
        it.bookingRef == bookingRef 
    }
}
