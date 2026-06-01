package com.example.data.mapper

import com.example.data.local.entity.BoardingPassEntity
import com.example.data.remote.dto.BoardingPassDto
import com.example.domain.model.BoardingPass
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private val isoFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}
private val isoFormatNoMillis = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US).apply {
    timeZone = TimeZone.getTimeZone("UTC")
}

private fun parseIso8601(value: String): Long {
    return try {
        isoFormat.parse(value)?.time ?: 0L
    } catch (_: Exception) {
        try {
            isoFormatNoMillis.parse(value)?.time ?: 0L
        } catch (_: Exception) {
            0L
        }
    }
}

// Entity → Domain

fun BoardingPassEntity.toDomain(): BoardingPass = BoardingPass(
    passId             = passId,
    uid                = uid,
    passengerId        = passengerId,
    flightId           = flightId,
    flightNumber       = flightNumber,
    origin             = origin,
    originCity         = originCity,
    destination        = destination,
    destinationCity    = destinationCity,
    passengerName      = passengerName,
    seatNumber         = seatNumber,
    gate               = gate,
    boardingTime       = boardingTime,
    departureTime      = departureTime,
    arrivalTime        = arrivalTime,
    bookingReference   = bookingReference,
    terminal           = terminal,
    qrCodeData         = qrCodeData,
    issuedAt           = issuedAt,
    lastSyncedAt       = lastSyncedAt,
    isSyncedWithServer = isSyncedWithServer
)

// Domain → Entity (uid obligatoire pour garantir le filtrage)

fun BoardingPass.toEntity(): BoardingPassEntity = BoardingPassEntity(
    passId             = passId,
    uid                = uid,
    passengerId        = passengerId,
    flightId           = flightId,
    flightNumber       = flightNumber,
    origin             = origin,
    originCity         = originCity,
    destination        = destination,
    destinationCity    = destinationCity,
    passengerName      = passengerName,
    seatNumber         = seatNumber,
    gate               = gate,
    boardingTime       = boardingTime,
    departureTime      = departureTime,
    arrivalTime        = arrivalTime,
    bookingReference   = bookingReference,
    terminal           = terminal,
    qrCodeData         = qrCodeData,
    issuedAt           = issuedAt,
    lastSyncedAt       = lastSyncedAt,
    isSyncedWithServer = isSyncedWithServer
)

// DTO → Domain (uid vient du contexte appelant, pas du DTO)

fun BoardingPassDto.toDomain(contextUid: String = ""): BoardingPass = BoardingPass(
    passId             = passId,
    uid                = if (uid.isNullOrEmpty()) contextUid else uid,
    passengerId        = passengerId,
    flightId           = flightId,
    flightNumber       = flightNumber,
    origin             = origin,
    originCity         = originCity,
    destination        = destination,
    destinationCity    = destinationCity,
    passengerName      = passengerName,
    seatNumber         = seatNumber,
    gate               = gate,
    boardingTime       = boardingTime,
    departureTime      = parseIso8601(departureTime),
    arrivalTime        = parseIso8601(arrivalTime),
    bookingReference   = bookingReference,
    terminal           = terminal,
    qrCodeData         = qrCode,
    issuedAt           = parseIso8601(issuedAt),
    isSyncedWithServer = true,
    lastSyncedAt       = System.currentTimeMillis()
)