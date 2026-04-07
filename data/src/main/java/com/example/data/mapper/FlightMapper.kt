package com.example.data.mapper

import com.example.data.remote.dto.FlightDto
import com.example.domain.model.Flight

fun FlightDto.toDomain(): Flight {
    return Flight(
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        destination = this.destination,
        departureTime = this.departureTime?.time ?: 0L,
        arrivalTime = this.arrivalTime?.time ?: 0L,
        aircraftType = this.aircraftType,
        status = this.status
    )
}
