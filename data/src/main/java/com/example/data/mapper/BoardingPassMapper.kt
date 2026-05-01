package com.example.data.mapper

import com.example.data.local.entity.BoardingPassEntity
import com.example.domain.model.BoardingPass

fun BoardingPassEntity.toDomain(): BoardingPass {
    return BoardingPass(
        passId = this.passId,
        passengerId = this.passengerId,
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        originCity = this.originCity,
        destination = this.destination,
        destinationCity = this.destinationCity,
        passengerName = this.passengerName,
        seatNumber = this.seatNumber,
        gate = this.gate,
        boardingTime = this.boardingTime,
        departureTime = this.departureTime,
        arrivalTime = this.arrivalTime,
        bookingReference = this.bookingReference,
        terminal = this.terminal,
        qrCodeData = this.qrCodeData,
        issuedAt = this.issuedAt,
        isSyncedWithServer = this.isSyncedWithServer
    )
}

fun BoardingPass.toEntity(): BoardingPassEntity {
    return BoardingPassEntity(
        passId = this.passId,
        passengerId = this.passengerId,
        flightId = this.flightId,
        flightNumber = this.flightNumber,
        origin = this.origin,
        originCity = this.originCity,
        destination = this.destination,
        destinationCity = this.destinationCity,
        passengerName = this.passengerName,
        seatNumber = this.seatNumber,
        gate = this.gate,
        boardingTime = this.boardingTime,
        departureTime = this.departureTime,
        arrivalTime = this.arrivalTime,
        bookingReference = this.bookingReference,
        terminal = this.terminal,
        qrCodeData = this.qrCodeData,
        issuedAt = this.issuedAt,
        isSyncedWithServer = this.isSyncedWithServer
    )
}