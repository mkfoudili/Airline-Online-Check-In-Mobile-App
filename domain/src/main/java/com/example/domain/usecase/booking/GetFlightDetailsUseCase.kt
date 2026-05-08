package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import com.example.domain.repository.FlightRepository
import javax.inject.Inject

class GetFlightDetailsUseCase @Inject constructor(
    private val bookingRepository: BookingRepository,
    private val flightRepository: FlightRepository
) {
    suspend operator fun invoke(uid: String, bookingRef: String): Result<Booking> {
        // 1. Fetch upcoming bookings to find the basic booking info
        val bookingsResult = bookingRepository.getUpcomingBookings(uid)
        
        return bookingsResult.mapCatching { bookings ->
            val foundBooking = bookings.find { it.bookingRef == bookingRef }
                ?: throw Exception("Booking with reference $bookingRef not found")
            
            // 2. Fetch fresh flight details from the flight service
            val flightResult = flightRepository.getFlightById(foundBooking.flight.flightId)
            
            // 3. Return the booking with updated flight info, or original if flight fetch fails
            flightResult.getOrNull()?.let { freshFlight ->
                foundBooking.copy(flight = freshFlight)
            } ?: foundBooking
        }
    }
}
