package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import javax.inject.Inject

class GetUpcomingBookingsUseCase @Inject constructor(
    private val repository: BookingRepository
) {
    suspend operator fun invoke(uid: String): Result<List<Booking>> {
        return repository.getUpcomingBookings(uid)
    }
}
