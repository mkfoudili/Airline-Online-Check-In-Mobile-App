package com.example.domain.usecase.ocr

import android.graphics.Bitmap
import com.example.domain.model.ParsedPassportData
import com.example.domain.repository.PassportOcrRepository
import javax.inject.Inject

/**
 * Orchestrates on-device OCR extraction from a passport bitmap.
 */
class ExtractPassportDataUseCase @Inject constructor(
    private val passportOcrRepository: PassportOcrRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): ParsedPassportData? {
        return passportOcrRepository.extractPassportData(bitmap)
    }
}
