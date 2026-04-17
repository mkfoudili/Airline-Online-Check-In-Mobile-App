package com.example.domain.model

data class Passenger(
    val passengerId: String,
    val uid: String?,
    val firstName: String,
    val lastName: String,
    val passportNumber: String?,
    val nationality: String?,
    val dateOfBirth: String?,
    val expiryDate: String?,
    val seatNumber: String?,
    val checkinStatus: String?
)
