package com.example.data.remote.dto

data class BaggageRequest(
    val checkedBaggageCount: Int,
    val specialEquipmentCount: Int
)

data class BaggageResponse(
    val success: Boolean,
    val message: String,
    val data: BaggageDataDto?
)

data class BaggageDataDto(
    val sessionId: String,
    val baggageDeclaration: BaggageDeclarationDto,
    val currentStep: String
)

data class BaggageDeclarationDto(
    val checkedBaggageCount: Int,
    val specialEquipmentCount: Int
)