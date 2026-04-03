package com.example.data.remote

data class SeatMapDto(
    val seatId: String,
    val flightId: String,
    val seatNumber: String,
    val seatClass: String?,
    val isAvailable: Boolean,
    val isPremium: Boolean,
    val occupiedBy: String?
)
