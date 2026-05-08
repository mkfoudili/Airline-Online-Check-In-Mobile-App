package com.example.domain.repository

import com.example.domain.model.Booking

interface BookingRepository {
    suspend fun getBooking(pnr: String, lastName: String): Result<Booking>
    suspend fun getBookingsByUid(uid: String): Result<List<Booking>>
    suspend fun getUpcomingBookings(uid: String): Result<List<Booking>>
}
