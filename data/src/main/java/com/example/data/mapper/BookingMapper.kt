package com.example.data.mapper

import com.example.data.remote.dto.PassengerDto
import com.example.domain.model.Passenger

fun PassengerDto.toDomain(): Passenger {
    return Passenger(
        passengerId = this.passengerId,
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
