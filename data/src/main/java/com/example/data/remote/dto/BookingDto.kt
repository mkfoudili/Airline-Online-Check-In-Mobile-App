package com.example.data.remote.dto

import java.sql.Timestamp

data class BookingDto(
    val bookingId: String,
    val uid: String,
    val pnr: String,
    val lastName: String,
    val status: String,
    val checkinDeadline: Timestamp?,
    val createdAt: Timestamp?
)
