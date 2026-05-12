package com.example.data.remote.dto

/**
 * DTO returned by GET /api/checkin/verify-passport
 * Matches the Prisma passenger model exactly.
 */
data class VerifyPassportResponseDto(
    val passenger: PassengerVerifyDto
)

data class PassengerVerifyDto(
    val passengerId: String,
    val bookingId: String,
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
