package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checkin_sessions")
data class CheckInSessionEntity(
    @PrimaryKey val sessionId: String,
    val passengerId: String,
    val bookingId: String,
    val currentStep: String?,
    val passportScanUrl: String?,
    val ocrValidation: String?,
    val baggageDeclaration: String?,
    val specialRequests: String?,
    val completedAt: Long?
)
