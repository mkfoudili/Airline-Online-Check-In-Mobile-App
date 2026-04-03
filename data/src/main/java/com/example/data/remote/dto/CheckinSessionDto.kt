package com.example.data.remote.dto

import java.sql.Timestamp

data class CheckinSessionDto(
    val sessionId: String,
    val passengerId: String,
    val bookingId: String,
    val currentStep: String?,
    val passportScanUrl: String?,
    val ocrValidation: String?,
    val baggageDeclaration: String?,
    val specialRequests: String?,
    val completedAt: Timestamp?
)
