package com.example.domain.usecase.checkin

import javax.inject.Inject

class SelectBaggageUseCase @Inject constructor() {
    operator fun invoke(
        checkedBaggageCount: Int,
        specialEquipmentCount: Int,
        onResult: (Result<Unit>) -> Unit
    ) {
        onResult(Result.success(Unit))
    }
}
