package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.PassengerVerifyDataSource
import com.example.data.remote.PassportVerificationException
import com.example.data.remote.dto.AdvanceStepRequest
import com.example.data.remote.dto.CheckinSessionDto
import com.example.data.remote.dto.CreateSessionRequest
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.repository.CheckInRepository
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val endpoint: Endpoint,
    private val passengerVerifyDataSource: PassengerVerifyDataSource
) : CheckInRepository {

    override suspend fun createOrResumeSession(
        passengerId: String,
        bookingId: String
    ): Result<CheckInSession> {
        return try {
            val response = endpoint.createOrResumeSession(
                CreateSessionRequest(passengerId = passengerId, bookingId = bookingId)
            )
            val dto: CheckinSessionDto = response.data
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun advanceSessionStep(
        passengerId: String,
        step: String
    ): Result<Unit> {
        return try {
            endpoint.advanceSessionStep(
                AdvanceStepRequest(passengerId = passengerId, step = step)
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun verifyPassport(
        passportNumber: String,
        lastName: String,
        firstName: String?,
        nationality: String?,
        dateOfBirth: String?,
        expiryDate: String?
    ): Result<Passenger> {
        return try {
            val dto = passengerVerifyDataSource.verifyPassport(
                passportNumber,
                lastName,
                firstName,
                nationality,
                dateOfBirth,
                expiryDate
            )
            Result.success(dto.toDomain())
        } catch (e: PassportVerificationException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(Exception("Network error. Please check your connection and try again."))
        }
    }

    override fun getPassengerForReview(): Passenger {
        return Passenger(
            passengerId    = "p_review",
            uid            = null,
            firstName      = "Batata",
            lastName       = "Sofiane",
            passportNumber = "A12345678",
            nationality    = "United Kingdom",
            dateOfBirth    = "14 May 1988",
            expiryDate     = "22 Nov 2031",
            seatNumber     = null,
            checkinStatus  = "PENDING"
        )
    }
}