package com.example.data.repository

import com.example.data.local.dao.CheckInSessionDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDto
import com.example.data.mapper.toEntity
import com.example.data.remote.CheckInDataSource
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger
import com.example.domain.model.SpecialRequests
import com.example.domain.repository.CheckInRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class CheckInRepositoryImpl @Inject constructor(
    private val checkInDataSource: CheckInDataSource,
    private val checkInSessionDao: CheckInSessionDao
) : CheckInRepository {

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
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localSession = checkInSessionDao.getSession(sessionId)
                if (localSession != null) {
                    callback(Result.success(localSession.toDomain()))
                } else {
                    checkInDataSource.getSession(sessionId) { result ->
                        result.onSuccess { sessionDto ->
                            val session = sessionDto.toDomain()
                            // Cache locally
                            CoroutineScope(Dispatchers.IO).launch {
                                checkInSessionDao.insertSession(session.toEntity())
                            }
                            callback(Result.success(session))
                        }.onFailure {
                            callback(Result.failure(it))
                        }
                    }
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        val entity = session.toEntity()
        
        checkInDataSource.updateSession(dto) { result ->
            result.onSuccess {
                CoroutineScope(Dispatchers.IO).launch {
                    checkInSessionDao.updateSession(entity)
                }
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        val entity = session.toEntity()

        checkInDataSource.createSession(dto) { result ->
            result.onSuccess {
                CoroutineScope(Dispatchers.IO).launch {
                    checkInSessionDao.insertSession(entity)
                }
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override suspend fun getUserPreferences(uid: String): Result<SpecialRequests> {
        return try {
            val dto = checkInDataSource.getUserPreferences(uid)
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
            val request = com.example.data.remote.dto.ConcludeCheckinRequest(
                passengerId = passengerId,
                uid = uid,
                preferredSoutien = specialRequests.preferredSoutien,
                preferredVisualsAudit = specialRequests.preferredVisualsAudit,
                preferredChildCare = specialRequests.preferredChildCare,
                preferredPetCare = specialRequests.preferredPetCare,
                mealPreference = specialRequests.mealPreference
            )
            val response = checkInDataSource.concludeCheckin(request)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
