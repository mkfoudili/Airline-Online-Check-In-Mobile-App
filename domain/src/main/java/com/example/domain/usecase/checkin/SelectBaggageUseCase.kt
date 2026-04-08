package com.example.domain.usecase.checkin

class SelectBaggageUseCase {
    operator fun invoke(
        checkedBaggageCount: Int,
        specialEquipmentCount: Int,
        onResult: (Result<Unit>) -> Unit
    ) {
        onResult(Result.success(Unit))
    }
}
