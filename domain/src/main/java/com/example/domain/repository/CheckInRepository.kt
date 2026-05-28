package com.example.domain.repository

import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.model.SpecialRequests

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

    suspend fun getUserPreferences(uid: String): Result<SpecialRequests>
    suspend fun concludeCheckin(
        passengerId: String,
        uid: String,
        specialRequests: SpecialRequests
    ): Result<Unit>
}