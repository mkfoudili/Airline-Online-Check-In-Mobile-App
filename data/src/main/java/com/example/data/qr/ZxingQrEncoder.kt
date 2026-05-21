package com.example.data.qr

import com.example.domain.usecase.boarding.GenerateQRCodeBitmapUseCase.QrMatrix
import com.example.domain.usecase.boarding.QrEncoder
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import javax.inject.Inject

/**
 * Data-layer implementation of [QrEncoder].
 * Uses ZXing to produce a [QrMatrix], a plain boolean grid with no Android dependencies.
 * The presentation layer converts the matrix to an [ImageBitmap] via [com.example.data.mapper.toBitmap].
 */
class ZxingQrEncoder @Inject constructor() : QrEncoder {

    override fun encode(data: String, size: Int): QrMatrix? {
        if (data.isBlank()) return null
        return try {
            val hints = mapOf<EncodeHintType, Any>(EncodeHintType.MARGIN to 1)
            val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints)
            val pixels = BooleanArray(size * size) { i -> bitMatrix[i % size, i / size] }
            QrMatrix(pixels = pixels, size = size)
        } catch (_: Exception) {
            null
        }
    }
}