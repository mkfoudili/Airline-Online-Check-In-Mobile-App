package com.example.data.remote.dto

import java.sql.Timestamp

data class FlightDto(
    val flightId: String,
    val flightNumber: String,
    val origin: String,
    val destination: String,
    val departureTime: Timestamp?,
    val arrivalTime: Timestamp?,
    val aircraftType: String?,
    val status: String?
)
