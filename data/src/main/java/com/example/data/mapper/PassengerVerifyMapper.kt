package com.example.data.mapper

import com.example.data.remote.dto.PassengerVerifyDto
import com.example.domain.model.Passenger

/**
 * Maps the verify-passport API response DTO to the domain [Passenger] model.
 * This DTO includes [expiryDate] which the standard PassengerDto omits.
 */
fun PassengerVerifyDto.toDomain(): Passenger = Passenger(
    passengerId   = this.passengerId,
    uid           = this.uid,
    firstName     = this.firstName,
    lastName      = this.lastName,
    passportNumber = this.passportNumber,
    nationality   = this.nationality,
    dateOfBirth   = this.dateOfBirth,
    expiryDate    = this.expiryDate,
    seatNumber    = this.seatNumber,
    checkinStatus = this.checkinStatus
)
