package com.example.domain.usecase.booking

import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository

class GetUpcomingBookingsUseCase(
    private val repository: BookingRepository
) {
    operator fun invoke(): List<Booking> = repository.getUpcomingBookings()
}
