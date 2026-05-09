package com.example.data.remote

import com.example.data.remote.dto.BookingDto
import com.example.data.remote.dto.PassengerDto
import com.example.data.remote.retrofit.Endpoint
import javax.inject.Inject

class BookingDataSource @Inject constructor(
    private val endpoint: Endpoint
) {

    suspend fun getBookingsByUid(uid: String): List<BookingDto> {
        return endpoint.getBookings(uid)
    }

    suspend fun getUpcomingBookingsByUid(uid: String): List<BookingDto> {
        return endpoint.getUpcomingBookings(uid)
    }

    /**
     * Recherche un booking par référence (PNR ou bookingRef) et nom de famille.
     * Retourne null si non trouvé (404) ou en cas d'erreur réseau.
     */
    suspend fun getBooking(pnr: String, lastName: String): BookingDto? {
        return try {
            endpoint.searchBooking(pnr.uppercase().trim(), lastName.trim())
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) null else throw e
        }
    }

    suspend fun getPassengersByBookingId(bookingId: String): List<PassengerDto> {
        return emptyList()
    }
}