package com.example.data.remote

import com.example.data.remote.dto.BookingDto
import com.example.data.remote.dto.PassengerDto
import com.example.data.remote.retrofit.ApiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookingDataSource @Inject constructor() {

    private val api = ApiService.api

    suspend fun getBookingsByUid(uid: String): List<BookingDto> {
        // Ignore the passed uid and use the hardcoded one as requested
        return api.getBookings(uid)
    }

    suspend fun getUpcomingBookingsByUid(uid: String): List<BookingDto> {
        return api.getUpcomingBookings(uid)
    }

    suspend fun getBooking(pnr: String, lastName: String): BookingDto? {
        // Still simulated or needs backend implementation
        return null
    }

    suspend fun getPassengersByBookingId(bookingId: String): List<PassengerDto> {
        return emptyList()
    }
}
