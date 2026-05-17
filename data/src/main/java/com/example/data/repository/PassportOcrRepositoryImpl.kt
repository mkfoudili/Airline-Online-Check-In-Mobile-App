package com.example.data.repository

import android.graphics.Bitmap
import com.example.data.remote.ocr.OcrDataSource
import com.example.domain.model.ParsedPassportData
import com.example.domain.repository.PassportOcrRepository
import javax.inject.Inject

class PassportOcrRepositoryImpl @Inject constructor(
    private val ocrDataSource: OcrDataSource
) : PassportOcrRepository {
    override suspend fun extractPassportData(bitmap: Bitmap): ParsedPassportData? {
        return ocrDataSource.extractPassportData(bitmap)
    }
}
