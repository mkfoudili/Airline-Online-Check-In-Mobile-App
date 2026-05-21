package com.example.domain.repository

import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger

interface CheckInRepository {

    suspend fun createOrResumeSession(
        passengerId: String,
        bookingId: String
    ): Result<CheckInSession>

    suspend fun advanceSessionStep(
        passengerId: String,
        step: String
    ): Result<Unit>

    suspend fun verifyPassport(
        passportNumber: String,
        lastName: String,
        firstName: String? = null,
        nationality: String? = null,
        dateOfBirth: String? = null,
        expiryDate: String? = null
    ): Result<Passenger>

    fun getPassengerForReview(): Passenger
}