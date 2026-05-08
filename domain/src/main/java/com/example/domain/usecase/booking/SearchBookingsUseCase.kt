package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

class SearchBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(
        uid: String,
        query: String = "",
        date: String? = null,
        status: String = "All"
    ): List<Booking> {
        val result = repository.getUpcomingBookings(uid)
        val allBookings = result.getOrDefault(emptyList())
        
        return allBookings.filter { booking ->
            val matchesQuery = query.isBlank() || 
                booking.flight.destinationCity.contains(query, ignoreCase = true) || 
                booking.flight.destination.contains(query, ignoreCase = true)
            
            val sdfDate = SimpleDateFormat("dd MMM", Locale.getDefault())
            val depDateStr = sdfDate.format(Date(booking.flight.departureTime))
            val matchesDate = date == null || depDateStr == date
            
            val matchesStatus = status == "All" || 
                booking.status.name.replace("_", " ").equals(status, ignoreCase = true)

            matchesQuery && matchesDate && matchesStatus
        }
    }
}
