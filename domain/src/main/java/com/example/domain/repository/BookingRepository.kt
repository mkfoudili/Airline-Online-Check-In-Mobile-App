package com.example.domain.repository

import com.example.domain.model.Booking

interface BookingRepository {
    fun getUpcomingBookings(): List<Booking>
}
