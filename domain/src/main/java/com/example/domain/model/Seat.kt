package com.example.domain.model

data class Seat(
    val seatId: String,
    val flightId: String,
    val seatNumber: String,
    val seatClass: String?,
    val isAvailable: Boolean,
    val isPremium: Boolean,
    val occupiedBy: String?
)
