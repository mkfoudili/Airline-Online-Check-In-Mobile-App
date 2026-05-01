package com.example.data.mapper

import android.graphics.Bitmap
import android.graphics.Color
import com.example.domain.usecase.boarding.GenerateQRCodeBitmapUseCase

fun GenerateQRCodeBitmapUseCase.QrMatrix.toBitmap(): Bitmap {
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (y in 0 until size) {
        for (x in 0 until size) {
            bmp.setPixel(x, y, if (pixels[y * size + x]) Color.BLACK else Color.WHITE)
        }
    }
    return bmp
}