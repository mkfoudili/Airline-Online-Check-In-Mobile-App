package com.example.data.remote.dto

import com.example.domain.model.CheckInStatus
import java.sql.Timestamp

data class BookingDto(
    val bookingId: String,
    val uid: String,
    val pnr: String,
    val lastName: String,
    val status: CheckInStatus,
    val checkinDeadline: Timestamp?,
    val createdAt: Timestamp?
)
