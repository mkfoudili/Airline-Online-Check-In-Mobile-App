package com.example.data.repository

import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.repository.BookingRepository

class BookingRepositoryImpl : BookingRepository {

    override fun getUpcomingBookings(): List<Booking> = listOf(
        Booking(
            bookingRef = "REF001",
            flightNumber = "BA342",
            origin = "LHR",
            destination = "CDG",
            departureDate = "Oct 28, 2023",
            departureTime = "22:40",
            status = CheckInStatus.CHECKED_IN
        ),
        Booking(
            bookingRef = "REF002",
            flightNumber = "BA342",
            origin = "LHR",
            destination = "CDG",
            departureDate = "Oct 28, 2023",
            departureTime = "22:30",
            status = CheckInStatus.CHECK_IN_OPEN
        ),
        Booking(
            bookingRef = "REF003",
            flightNumber = "BA342",
            origin = "LHR",
            destination = "CDG",
            departureDate = "Oct 30, 2023",
            departureTime = "22:08",
            status = CheckInStatus.CONFIRMED
        )
    )
}
