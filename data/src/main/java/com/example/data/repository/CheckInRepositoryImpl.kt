package com.example.data.repository

import com.example.data.remote.CheckInDataSource
import com.example.data.remote.dto.CheckinSessionDto
import com.example.domain.model.CheckInSession
import com.example.domain.repository.CheckInRepository
import java.sql.Timestamp

class CheckInRepositoryImpl(private val checkInDataSource: CheckInDataSource) : CheckInRepository {

    override fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit) {
        checkInDataSource.getSession(sessionId) { result ->
            result.onSuccess { sessionDto ->
                callback(Result.success(sessionDto.toDomain()))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        checkInDataSource.updateSession(dto) { result ->
            result.onSuccess {
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        checkInDataSource.createSession(dto) { result ->
            result.onSuccess {
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    private fun CheckinSessionDto.toDomain(): CheckInSession {
        return CheckInSession(
            sessionId = this.sessionId,
            passengerId = this.passengerId,
            bookingId = this.bookingId,
            currentStep = this.currentStep ?: "",
            passportScanUrl = this.passportScanUrl,
            ocrValidation = this.ocrValidation,
            baggageDeclaration = null,
            specialRequests = null,
            completedAt = this.completedAt?.time
        )
    }

    private fun CheckInSession.toDto(): CheckinSessionDto {
        return CheckinSessionDto(
            sessionId = this.sessionId,
            passengerId = this.passengerId,
            bookingId = this.bookingId,
            currentStep = this.currentStep,
            passportScanUrl = this.passportScanUrl,
            ocrValidation = this.ocrValidation,
            baggageDeclaration = this.baggageDeclaration?.toString(),
            specialRequests = this.specialRequests?.toString(),
            completedAt = this.completedAt?.let { Timestamp(it) }
        )
    }
}
