package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import javax.inject.Inject

class GetUpcomingBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(uid: String): List<Booking> {
        val result = repository.getUpcomingBookings(uid)
        return result.getOrDefault(emptyList())
    }
}
