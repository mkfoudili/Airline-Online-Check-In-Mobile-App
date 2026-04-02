package com.example.domain.model

data class Booking(
    val bookingRef: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String = "",        // e.g. "SAN FRANCISCO"
    val destination: String,
    val destinationCity: String = "",   // e.g. "LONDON"
    val departureDate: String,
    val departureTime: String,
    val duration: String = "",          // e.g. "11h 20m"
    val status: CheckInStatus
)
