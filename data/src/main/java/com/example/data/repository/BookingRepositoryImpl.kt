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
            originCity = "London",
            destination = "CDG",
            destinationCity = "Paris",
            departureDate = "Oct 28, 2023",
            departureTime = "22:40",
            duration = "1h 15m",
            passengerName = "Djerfi Fatma",
            pnr = "BB9XC2",
            checkInOpensTime = "20:40",
            boardingTime = "22:10",
            gate = "A12",
            status = CheckInStatus.CHECKED_IN
        ),
        Booking(
            bookingRef = "REF002",
            flightNumber = "BA342",
            origin = "LHR",
            originCity = "London",
            destination = "CDG",
            destinationCity = "Paris",
            departureDate = "Oct 28, 2023",
            departureTime = "22:30",
            duration = "1h 15m",
            passengerName = "Djerfi Fatma",
            pnr = "BB9XC2",
            checkInOpensTime = "20:30",
            boardingTime = "22:00",
            gate = "B04",
            status = CheckInStatus.CHECK_IN_OPEN
        ),
        Booking(
            bookingRef = "REF003",
            flightNumber = "BA342",
            origin = "LHR",
            originCity = "London",
            destination = "CDG",
            destinationCity = "Paris",
            departureDate = "Oct 30, 2023",
            departureTime = "22:08",
            duration = "1h 15m",
            passengerName = "Djerfi Fatma",
            pnr = "BB9XC2",
            checkInOpensTime = "20:08",
            boardingTime = "21:38",
            gate = "C22",
            status = CheckInStatus.CONFIRMED
        )
    )
}
