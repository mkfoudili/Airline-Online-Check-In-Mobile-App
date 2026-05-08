package com.example.data.remote.dto

import com.example.domain.model.CheckInStatus
import java.util.Date

data class BookingDto(
    val bookingId: String,
    val uid: String,
    val pnr: String,
    val lastName: String,
    val bookingRef: String,
    val status: CheckInStatus,
    val checkinDeadline: Date?,
    val createdAt: Date?,
    val flight: FlightDto,
    val passengers: List<PassengerDto> = emptyList()
)
