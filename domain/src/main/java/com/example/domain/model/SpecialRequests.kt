package com.example.domain.model

data class SpecialRequests(
    val preferredSoutien: Boolean = false,
    val preferredVisualsAudit: Boolean = false,
    val preferredChildCare: Boolean = false,
    val preferredPetCare: Boolean = false,
    val mealPreference: Boolean = false
)
