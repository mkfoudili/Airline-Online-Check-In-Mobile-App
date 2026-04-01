package com.example.domain.model

data class SpecialRequests(
    val dietaryPreference: String?,
    val needsAssistance: Boolean,
    val infantOnBoard: Boolean,
    val petOnBoard: Boolean
)
