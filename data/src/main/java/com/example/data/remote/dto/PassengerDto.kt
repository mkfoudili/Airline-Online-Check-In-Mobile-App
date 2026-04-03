package com.example.data.remote.dto

data class PassengerDto(
    val passengerId: String,
    val bookingId: String,
    val uid: String?,
    val firstName: String,
    val lastName: String,
    val passportNumber: String?,
    val nationality: String?,
    val dateOfBirth: String?,
    val seatNumber: String?,
    val checkinStatus: String?
)
