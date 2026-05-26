package com.example.data.mapper

import com.example.data.remote.dto.BookingDto
import com.example.data.remote.dto.PassengerDto
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Passenger

fun BookingDto.toDomain(): Booking = Booking(
    bookingId           = bookingId,
    pnr                 = pnr,
    lastName            = lastName,
    status              = status ?: CheckInStatus.CONFIRMED,
    flight              = flight.toDomain(),
    passengers          = passengers.map { it.toDomain() },
    bookingRef          = bookingRef,
    checkinPassengerId  = checkinSession?.passengerId,
    checkinSessionId    = checkinSession?.sessionId
)

fun PassengerDto.toDomain(): Passenger = Passenger(
    passengerId    = passengerId,
    uid            = uid,
    firstName      = firstName,
    lastName       = lastName,
    passportNumber = passportNumber,
    nationality    = nationality,
    dateOfBirth    = dateOfBirth,
    expiryDate     = null,
    seatNumber     = seatNumber,
    checkinStatus  = checkinStatus
)