package com.example.domain.usecase.checkin

import com.example.domain.model.BaggageDeclaration
import com.example.domain.repository.CheckInRepository
import javax.inject.Inject

class SelectBaggageUseCase @Inject constructor(
    private val repository: CheckInRepository
) {
    suspend operator fun invoke(
        checkedBaggageCount: Int,
        specialEquipmentCount: Int
    ): Result<Unit> {
        return repository.declareBaggage(
            BaggageDeclaration(
                checkedBaggageCount = checkedBaggageCount,
                specialEquipmentCount = specialEquipmentCount
            )
        )
    }
}
