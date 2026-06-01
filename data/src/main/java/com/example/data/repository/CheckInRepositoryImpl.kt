package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.PassengerVerifyDataSource
import com.example.data.remote.PassportVerificationException
import com.example.data.remote.dto.AdvanceStepRequest
import com.example.data.remote.dto.CheckinSessionDto
import com.example.data.remote.dto.ConcludeCheckinRequest
import com.example.data.remote.dto.CreateSessionRequest
import com.example.data.remote.dto.BaggageRequest
import com.example.data.remote.dto.BaggageResponse
import com.example.data.remote.retrofit.Endpoint
import com.example.data.security.SecureStorage
import com.example.domain.model.BaggageDeclaration
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.model.SpecialRequests
import com.example.domain.repository.CheckInRepository
import retrofit2.Response
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val endpoint: Endpoint,
    private val passengerVerifyDataSource: PassengerVerifyDataSource,
    private val secureStorage: SecureStorage
) : CheckInRepository {

    override suspend fun createOrResumeSession(
        passengerId: String,
        bookingId: String,
        uid: String?
    ): Result<CheckInSession> {
        return try {
            val response = endpoint.createOrResumeSession(
                CreateSessionRequest(passengerId = passengerId, bookingId = bookingId, uid = uid)
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
            bookingId      = "b_review",
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

    override suspend fun getUserPreferences(uid: String): Result<SpecialRequests> {
        return try {
            val dto = endpoint.getUserPreferences(uid)
            Result.success(
                SpecialRequests(
                    preferredSoutien = dto.preferredSoutien,
                    preferredVisualsAudit = dto.preferredVisualsAudit,
                    preferredChildCare = dto.preferredChildCare,
                    preferredPetCare = dto.preferredPetCare,
                    mealPreference = dto.mealPreference
                )
            )
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun concludeCheckin(
        passengerId: String,
        uid: String,
        specialRequests: SpecialRequests
    ): Result<Unit> {
        return try {
            val request = ConcludeCheckinRequest(
                passengerId = passengerId,
                uid = uid,
                preferredSoutien = specialRequests.preferredSoutien,
                preferredVisualsAudit = specialRequests.preferredVisualsAudit,
                preferredChildCare = specialRequests.preferredChildCare,
                preferredPetCare = specialRequests.preferredPetCare,
                mealPreference = specialRequests.mealPreference
            )
            val response = endpoint.concludeCheckin(request)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun declareBaggage(declaration: BaggageDeclaration): Result<Unit> {
        return try {
            val request = BaggageRequest(
                checkedBaggageCount = declaration.checkedBaggageCount,
                specialEquipmentCount = declaration.specialEquipmentCount
            )
            val token = secureStorage.getAuthToken() ?: throw Exception("Not authenticated")
            val response: Response<BaggageResponse> = endpoint.declareBaggage("Bearer $token", request)
            if (response.isSuccessful) {
                val body = response.body()
                if (body?.success == true) {
                    Result.success(Unit)
                } else {
                    Result.failure(Exception(body?.message ?: "Failed to declare baggage"))
                }
            } else {
                val errorMsg = response.errorBody()?.string() ?: "Error: ${response.code()}"
                Result.failure(Exception(errorMsg))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
