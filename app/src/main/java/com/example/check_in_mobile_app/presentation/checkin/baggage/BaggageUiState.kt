package com.example.check_in_mobile_app.presentation.checkin.baggage

data class BaggageUiState(
    val checkedBaggageCount: Int = 0,
    val specialEquipmentCount: Int = 0,
    val isLoading: Boolean = false,
    val error: String? = null
)
