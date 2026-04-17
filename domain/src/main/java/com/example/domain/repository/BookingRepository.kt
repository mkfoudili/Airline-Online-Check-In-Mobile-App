package com.example.domain.repository

import com.example.domain.model.Booking

interface BookingRepository {
    fun getBooking(pnr: String, lastName: String, callback: (Result<Booking>) -> Unit)
    fun getBookingsByUid(uid: String, callback: (Result<List<Booking>>) -> Unit)
    fun getUpcomingBookings(): List<Booking>
}
