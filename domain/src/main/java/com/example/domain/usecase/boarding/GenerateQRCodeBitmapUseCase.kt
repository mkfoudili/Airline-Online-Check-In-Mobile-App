package com.example.domain.usecase.boarding

import javax.inject.Inject

/**
 * Produces a platform-agnostic QR matrix from a string payload
 */
class GenerateQRCodeBitmapUseCase @Inject constructor(
    private val encoder: QrEncoder
) {
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
        return encoder.encode(data, size)
    }
}

/**
 * Domain-owned interface for QR code encoding.
 * Implemented in the data layer using ZXing ([com.example.data.qr.ZxingQrEncoder]).
 */
interface QrEncoder {
    fun encode(data: String, size: Int): GenerateQRCodeBitmapUseCase.QrMatrix?
}