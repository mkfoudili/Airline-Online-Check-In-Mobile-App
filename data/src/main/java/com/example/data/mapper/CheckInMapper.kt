package com.example.data.mapper

import com.example.data.remote.dto.CheckinSessionDto
import com.example.domain.model.CheckInSession
import java.sql.Timestamp

fun CheckinSessionDto.toDomain(): CheckInSession {
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

fun CheckInSession.toDto(): CheckinSessionDto {
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
