package com.example.domain.model

data class FlightItinerary(
    val booking: Booking,
    val flight: Flight,
    val passengers: List<Passenger>,
    val checkInOpen: Boolean,
    val checkInDeadline: Long
)
