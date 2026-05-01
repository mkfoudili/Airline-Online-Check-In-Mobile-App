package com.example.domain.usecase.boarding

import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter

class GenerateQRCodeBitmapUseCase {

    data class QrMatrix(
        val pixels: BooleanArray,
        val size: Int
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other !is QrMatrix) return false
            return size == other.size && pixels.contentEquals(other.pixels)
        }

        override fun hashCode(): Int {
            var result = pixels.contentHashCode()
            result = 31 * result + size
            return result
        }
    }

    operator fun invoke(data: String, size: Int = 512): QrMatrix? {
        if (data.isBlank()) return null
        return try {
            val hints = mapOf<EncodeHintType, Any>(EncodeHintType.MARGIN to 1)
            val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints)
            val pixels = BooleanArray(size * size) { i ->
                bitMatrix[i % size, i / size]
            }
            QrMatrix(pixels = pixels, size = size)
        } catch (_: Exception) {
            null
        }
    }
}