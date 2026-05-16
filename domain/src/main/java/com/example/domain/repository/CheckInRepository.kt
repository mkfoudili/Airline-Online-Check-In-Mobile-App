package com.example.domain.repository

import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.model.SpecialRequests

interface CheckInRepository {
    fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit)
    fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
    fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
    fun getPassengerForReview(): Passenger

    suspend fun getUserPreferences(uid: String): Result<SpecialRequests>
    suspend fun concludeCheckin(
        passengerId: String,
        uid: String,
        specialRequests: SpecialRequests
    ): Result<Unit>
}