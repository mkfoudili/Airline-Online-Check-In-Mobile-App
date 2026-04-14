package com.example.domain.model

data class Flight(
    val flightId: String,
    val flightNumber: String,
    val origin: String,
    val destination: String,
    val departureTime: Long,
    val arrivalTime: Long,
    val aircraftType: String?,
    val status: String?
)
