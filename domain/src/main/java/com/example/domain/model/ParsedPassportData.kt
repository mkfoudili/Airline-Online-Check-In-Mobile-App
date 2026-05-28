package com.example.domain.model


data class ParsedPassportData(
    val passportNumber: String?,
    val nationality: String?,
    val dateOfBirth: String?,   // formatted as YYYY-MM-DD
    val expiryDate: String?,    // formatted as YYYY-MM-DD
    val firstName: String?,
    val lastName: String?,
    val rawText: String? = null
)
