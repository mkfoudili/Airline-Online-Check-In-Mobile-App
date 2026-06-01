package com.example.data.mapper

import com.example.data.remote.dto.CheckinSessionDto
import com.example.domain.model.CheckInSession
import com.example.domain.model.CheckInStatus
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import java.sql.Timestamp

fun CheckinSessionDto.toDomain(): CheckInSession {
    return CheckInSession(
        sessionId = this.sessionId,
        passengerId = this.passengerId,
        bookingId = this.bookingId,
        uid = this.uid,
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
        uid = this.uid,
        currentStep = this.currentStep,
        passportScanUrl = this.passportScanUrl,
        ocrValidation = this.ocrValidation,
        baggageDeclaration = this.baggageDeclaration?.toString(),
        specialRequests = this.specialRequests?.toString(),
        completedAt = this.completedAt?.let { Timestamp(it) }
    )
}

class CheckInStatusDeserializer : JsonDeserializer<CheckInStatus?> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): CheckInStatus? {
        val raw = json?.asString ?: return null
        val normalized = raw.uppercase().replace("-", "_").trim()
        return try {
            CheckInStatus.valueOf(normalized)
        } catch (e: IllegalArgumentException) {
            null
        }
    }
}
