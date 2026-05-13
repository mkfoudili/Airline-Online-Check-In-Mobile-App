package com.example.data.repository

import com.example.data.remote.dto.BaggageRequest

import com.example.data.remote.retrofit.Endpoint
import com.example.data.security.SecureStorage
import com.example.domain.model.BaggageDeclaration
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.repository.CheckInRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val api: Endpoint,
    private val secureStorage: SecureStorage
) : CheckInRepository {

    override suspend fun declareBaggage(
        passengerId: String,
        baggageCount: Int,
        specialCount: Int
    ): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val token = secureStorage.getAuthToken()
                ?: return@withContext Result.failure(Exception("Not authenticated"))

            val request = BaggageRequest( baggageCount, specialCount)
            val response = api.declareBaggage("Bearer $token", request)

            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Baggage declaration failed"))
                }
            } else {
                Result.failure(Exception("Error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getBaggageDeclaration(): BaggageDeclaration {
        TODO("Not yet implemented")
    }

    override fun getPassengerForReview(): Passenger {
        return Passenger(
            passengerId = "p_review",
            uid = null,
            firstName = "Batata",
            lastName = "Sofiane",
            passportNumber = "A12345678",
            nationality = "United Kingdom",
            dateOfBirth = "14 May 1988",
            expiryDate = "22 Nov 2031",
            seatNumber = null,
            checkinStatus = "PENDING"
        )
    }

    override fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit) {
        // Implementation for session retrieval
    }

    override fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        // Implementation for updating session
    }

    override fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        // Implementation for creating session
    }
}