package com.example.domain.usecase.checkin

import com.example.domain.model.Passenger
import com.example.domain.repository.CheckInRepository
import javax.inject.Inject

/**
 * Verifies OCR-extracted passport data against the backend DB.
 */
class VerifyPassportUseCase @Inject constructor(
    private val checkInRepository: CheckInRepository
) {
    suspend operator fun invoke(
        passportNumber: String,
        lastName: String,
        firstName: String? = null,
        nationality: String? = null,
        dateOfBirth: String? = null,
        expiryDate: String? = null
    ): Result<Passenger> {
        return checkInRepository.verifyPassport(
            passportNumber, 
            lastName, 
            firstName, 
            nationality,
            dateOfBirth,
            expiryDate
        )
    }
}
