package com.example.data.remote

import com.example.data.remote.dto.BookingDto
import com.example.data.remote.dto.PassengerDto
import com.example.domain.model.CheckInStatus
import java.util.*
import javax.inject.Inject

class BookingDataSource @Inject constructor() {

    /**
     * Fetches a booking by its PNR and the last name of the traveler.
     * Simulated to return a static booking.
     */
    fun getBooking(pnr: String, lastName: String, callback: (Result<BookingDto>) -> Unit) {
        // Simulating network delay
        val booking = BookingDto(
            bookingId = "B123",
            uid = "user_1",
            pnr = pnr.uppercase(),
            lastName = lastName,
            status = CheckInStatus.CHECK_IN_OPEN,
            checkinDeadline = Date(System.currentTimeMillis() + 86400000), // 24 hours from now
            createdAt = Date()
        )
        callback(Result.success(booking))
    }

    /**
     * Fetches all bookings associated with a specific user ID.
     * Simulated to return a static list.
     */
    fun getBookingsByUid(uid: String, callback: (Result<List<BookingDto>>) -> Unit) {
        val bookings = listOf(
            BookingDto(
                bookingId = "B123",
                uid = uid,
                pnr = "ABC123",
                lastName = "Doe",
                status = CheckInStatus.CHECK_IN_OPEN,
                checkinDeadline = Date(System.currentTimeMillis() + 86400000),
                createdAt = Date()
            ),
            BookingDto(
                bookingId = "B456",
                uid = uid,
                pnr = "XYZ789",
                lastName = "Doe",
                status = CheckInStatus.CONFIRMED,
                checkinDeadline = Date(System.currentTimeMillis() + 172800000),
                createdAt = Date()
            )
        )
        callback(Result.success(bookings))
    }

    /**
     * Fetches all passengers associated with a specific booking ID.
     * Simulated to return static passengers.
     */
    fun getPassengersByBookingId(bookingId: String, callback: (Result<List<PassengerDto>>) -> Unit) {
        val passengers = listOf(
            PassengerDto(
                passengerId = "P1",
                bookingId = bookingId,
                uid = "user_1",
                firstName = "John",
                lastName = "Doe",
                passportNumber = "A12345678",
                nationality = "US",
                dateOfBirth = "1990-01-01",
                seatNumber = "12A",
                checkinStatus = "PENDING"
            )
        )
        callback(Result.success(passengers))
    }
}
