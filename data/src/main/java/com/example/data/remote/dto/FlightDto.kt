package com.example.data.remote.dto

import java.util.Date

data class FlightDto(
    val flightId: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String,
    val destination: String,
    val destinationCity: String,
    val departureTime: Date?,
    val arrivalTime: Date?,
    val aircraftType: String?,
    val status: String?,
    val boardingTime: String?,
    val checkInOpensTime: String?,
    val gate: String?,
    val terminal: String?
)
