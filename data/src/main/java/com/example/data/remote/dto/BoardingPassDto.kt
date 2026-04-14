package com.example.data.remote.dto

import java.sql.Timestamp

data class BoardingPassDto(
    val passId: String,
    val passengerId: String,
    val flightId: String,
    val qrCode: String?,
    val seatNumber: String?,
    val gate: String?,
    val boardingTime: String?,
    val issuedAt: Timestamp?
)
