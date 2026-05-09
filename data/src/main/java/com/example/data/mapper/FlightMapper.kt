package com.example.data.mapper

import com.example.data.local.entity.FlightEntity
import com.example.data.remote.dto.FlightDto
import com.example.domain.model.Flight

fun FlightDto.toDomain(): Flight {
    return Flight(
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        originCity = this.originCity,
        destination = this.destination,
        destinationCity = this.destinationCity,
        departureTime = this.departureTime?.time ?: 0L,
        arrivalTime = this.arrivalTime?.time ?: 0L,
        aircraftType = this.aircraftType,
        status = this.status,
        boardingTime = this.boardingTime ?: "",
        checkInOpensTime = this.checkInOpensTime ?: "",
        gate = this.gate ?: "",
        terminal = this.terminal ?: ""
    )
}

fun FlightEntity.toDomain(): Flight {
    return Flight(
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        originCity = this.originCity,
        destination = this.destination,
        destinationCity = this.destinationCity,
        departureTime = this.departureTime ?: 0L,
        arrivalTime = this.arrivalTime ?: 0L,
        aircraftType = this.aircraftType,
        status = this.status,
        gate = this.gate ?: "",
        terminal = this.terminal ?: "",
        boardingTime = this.boardingTime ?: "",
        checkInOpensTime = this.checkInOpensTime ?: ""
    )
}

fun Flight.toEntity(): FlightEntity {
    return FlightEntity(
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        originCity = this.originCity,
        destination = this.destination,
        destinationCity = this.destinationCity,
        departureTime = this.departureTime,
        arrivalTime = this.arrivalTime,
        aircraftType = this.aircraftType,
        status = this.status,
        gate = this.gate.ifBlank { null },
        terminal = this.terminal.ifBlank { null },
        boardingTime = this.boardingTime.ifBlank { null },
        checkInOpensTime = this.checkInOpensTime.ifBlank { null },
        lastSyncedAt = System.currentTimeMillis()
    )
}