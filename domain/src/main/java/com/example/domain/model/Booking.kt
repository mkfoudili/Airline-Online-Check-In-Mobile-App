package com.example.domain.model

data class Booking(
    val bookingId: String,
    val pnr: String,
    val lastName: String,
    val status: String,
    val flight: Flight,
    val passengers: List<Passenger>
)
// to fix passengers