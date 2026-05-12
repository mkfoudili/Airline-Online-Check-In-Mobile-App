package com.example.domain.repository

import android.graphics.Bitmap
import com.example.domain.model.ParsedPassportData

/**
 * Interface for on-device OCR passport extraction.
 */
interface PassportOcrRepository {
    /**
     * Extracts MRZ data from a passport image.
     */
    suspend fun extractPassportData(bitmap: Bitmap): ParsedPassportData?
}
