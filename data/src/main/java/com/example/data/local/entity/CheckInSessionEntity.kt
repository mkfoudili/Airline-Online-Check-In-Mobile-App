package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkin_sessions")
data class CheckInSessionEntity(
    @PrimaryKey val sessionId: String,
    val bookingId: String,
    val flightId: String,
    val startTime: Long,
    val lastUpdateTime: Long,
    val status: String,
    val selectedSeatId: String? = null
)
// + passengerId + currentStep + passportScanUrl + ocrValidation + baggageDeclaration + specialRequests + completedAt
// - last update time - selectedSeatId - passportScanUrl