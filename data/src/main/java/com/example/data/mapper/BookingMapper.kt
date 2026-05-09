package com.example.data.mapper

import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.FlightItineraryEntity
import com.example.data.local.entity.PassengerEntity
import com.example.data.remote.dto.PassengerDto
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.FlightItinerary
import com.example.domain.model.Passenger
import com.example.data.remote.dto.BookingDto

fun BookingDto.toDomain(): Booking {
    return Booking(
        bookingId = this.bookingId,
        pnr = this.pnr,
        lastName = this.lastName,
        status = this.status,
        flight = this.flight.toDomain(),
        passengers = this.passengers.map { it.toDomain() },
        bookingRef = this.bookingRef
    )
}

fun PassengerDto.toDomain(): Passenger {
    return Passenger(
        passengerId = this.passengerId,
        uid = this.uid,
        firstName = this.firstName,
        lastName = this.lastName,
        passportNumber = this.passportNumber,
        nationality = this.nationality,
        dateOfBirth = this.dateOfBirth,
        expiryDate = null,
        seatNumber = this.seatNumber,
        checkinStatus = this.checkinStatus
    )
}

fun PassengerEntity.toDomain(): Passenger {
    return Passenger(
        passengerId = this.passengerId,
        uid = this.uid,
        firstName = this.firstName,
        lastName = this.lastName,
        passportNumber = this.passportNumber,
        nationality = this.nationality,
        dateOfBirth = this.dateOfBirth,
        expiryDate = null,
        seatNumber = this.seatNumber,
        checkinStatus = this.checkinStatus
    )
}

fun Passenger.toEntity(bookingId: String): PassengerEntity {
    return PassengerEntity(
        passengerId = this.passengerId,
        bookingId = bookingId,
        uid = this.uid,
        firstName = this.firstName,
        lastName = this.lastName,
        passportNumber = this.passportNumber,
        nationality = this.nationality,
        dateOfBirth = this.dateOfBirth,
        seatNumber = this.seatNumber,
        checkinStatus = this.checkinStatus
    )
}

fun BookingEntity.toDomain(flight: Flight, passengers: List<Passenger>): Booking {
    return Booking(
        bookingId = this.bookingId,
        pnr = this.pnr,
        lastName = this.lastName,
        status = try {
            CheckInStatus.valueOf(this.status.uppercase())
        } catch (e: Exception) {
            CheckInStatus.CONFIRMED
        },
        flight = flight,
        passengers = passengers,
        bookingRef = this.bookingRef
    )
}

fun Booking.toEntity(uid: String = ""): BookingEntity {
    return BookingEntity(
        bookingId = this.bookingId,
        flightId = this.flight.flightId,
        uid = uid,
        pnr = this.pnr,
        bookingRef = this.bookingRef,
        lastName = this.lastName,
        status = this.status.name,
        checkinDeadline = this.flight.departureTime - 3_600_000L, // 1h avant le départ
        createdAt = System.currentTimeMillis(),
        lastSyncedAt = System.currentTimeMillis()
    )
}

fun FlightItineraryEntity.toDomain(): FlightItinerary {
    val domainFlight = flight.toDomain()
    val domainPassengers = passengers.map { it.toDomain() }
    return FlightItinerary(
        booking = booking.toDomain(domainFlight, domainPassengers),
        flight = domainFlight,
        passengers = domainPassengers,
        checkInOpen = (booking.checkinDeadline ?: 0L) > System.currentTimeMillis(),
        checkInDeadline = booking.checkinDeadline ?: 0L
    )
}