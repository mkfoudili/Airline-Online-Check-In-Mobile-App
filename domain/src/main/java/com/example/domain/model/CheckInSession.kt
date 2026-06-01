package com.example.domain.model

data class CheckInSession(
    val sessionId: String,
    val passengerId: String,
    val bookingId: String,
    val uid: String?, // Link to the user who performed the check-in
    val currentStep: String,
    val passportScanUrl: String?,
    val ocrValidation: String?,
    val baggageDeclaration: BaggageDeclaration?,
    val specialRequests: SpecialRequests?,
    val completedAt: Long?
)
