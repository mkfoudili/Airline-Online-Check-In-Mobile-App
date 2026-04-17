package com.example.domain.model

data class Booking(
    val bookingId: String,
    val pnr: String,
    val lastName: String,
    val status: CheckInStatus,
    val flight: Flight,
    val passengers: List<Passenger>,
    val gate: String = "",
    val bookingRef: String = ""
)
