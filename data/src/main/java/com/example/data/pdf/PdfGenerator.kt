package com.example.data.pdf

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import com.example.domain.usecase.boarding.PdfBoardingPassData
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.OutputStream

/**
 * Generates a PDF boarding pass using Android's built-in PdfDocument API.
 * Uses ZXing to render the QR code bitmap inline.
 * Saves to the Downloads folder via MediaStore (API 29+) or legacy path.
 */
object PdfGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842

    fun generate(context: Context, data: PdfBoardingPassData): Result<String> {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            val page = document.startPage(pageInfo)
            drawBoardingPass(page.canvas, data)
            document.finishPage(page)

            val fileName = "boarding_pass_${data.bookingReference}.pdf"
            val outputStream = openOutputStream(context, fileName)
                ?: return Result.failure(Exception("Cannot open output stream"))

            outputStream.use { document.writeTo(it) }
            document.close()

            Result.success(fileName)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun drawBoardingPass(canvas: Canvas, data: PdfBoardingPassData) {
        val navyBlue = Color.rgb(26, 54, 93)
        val lightGray = Color.rgb(248, 250, 252)
        val subtleGray = Color.rgb(100, 116, 139)
        val white = Color.WHITE

        val margin = 40f
        val contentWidth = PAGE_WIDTH - 2 * margin

        // ── Header background ──
        val headerPaint = Paint().apply { color = navyBlue }
        canvas.drawRect(margin, 40f, PAGE_WIDTH - margin, 110f, headerPaint)

        // Header text
        val whiteTextPaint = Paint().apply {
            color = white
            textSize = 14f
            isFakeBoldText = true
        }
        canvas.drawText("SKYWIRE AIRLINES", margin + 16f, 70f, whiteTextPaint)
        val flightPaint = Paint().apply { color = Color.argb(200, 255, 255, 255); textSize = 11f }
        canvas.drawText("FLIGHT ${data.flightNumber}", PAGE_WIDTH - margin - 90f, 70f, flightPaint)
        val datePaint = Paint().apply { color = Color.argb(180, 255, 255, 255); textSize = 10f }
        canvas.drawText(data.departureDate, margin + 16f, 92f, datePaint)

        // ── Route row ──
        val boldNavy = Paint().apply { color = navyBlue; textSize = 36f; isFakeBoldText = true }
        val subtlePaint = Paint().apply { color = subtleGray; textSize = 10f }

        canvas.drawText(data.origin, margin, 165f, boldNavy)
        canvas.drawText(data.originCity.uppercase(), margin, 180f, subtlePaint)

        // Arrow in center
        val centerX = PAGE_WIDTH / 2f
        val arrowPaint = Paint().apply { color = subtleGray; strokeWidth = 2f; style = Paint.Style.STROKE }
        canvas.drawLine(margin + 80f, 155f, centerX - 20f, 155f, arrowPaint)
        canvas.drawLine(centerX + 20f, 155f, PAGE_WIDTH - margin - 80f, 155f, arrowPaint)
        val arrowHead = Paint().apply { color = navyBlue; textSize = 18f; isFakeBoldText = true }
        canvas.drawText("✈", centerX - 10f, 162f, arrowHead)

        val rightAlign = Paint().apply {
            color = navyBlue; textSize = 36f; isFakeBoldText = true
            textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(data.destination, PAGE_WIDTH - margin, 165f, rightAlign)
        val subtleRight = Paint().apply {
            color = subtleGray; textSize = 10f; textAlign = Paint.Align.RIGHT
        }
        canvas.drawText(data.destinationCity.uppercase(), PAGE_WIDTH - margin, 180f, subtleRight)

        // ── Dashed separator ──
        val dashedPaint = Paint().apply {
            color = Color.rgb(203, 213, 225)
            strokeWidth = 1.5f
            style = Paint.Style.STROKE
            pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f, 6f), 0f)
        }
        canvas.drawLine(margin, 200f, PAGE_WIDTH - margin, 200f, dashedPaint)

        // ── Flight details grid ──
        val labelPaint = Paint().apply { color = subtleGray; textSize = 9f }
        val valuePaint = Paint().apply { color = navyBlue; textSize = 22f; isFakeBoldText = true }

        // Row 1: Gate | Seat
        canvas.drawText("GATE", margin, 230f, labelPaint)
        canvas.drawText(data.gate, margin, 255f, valuePaint)

        canvas.drawText("SEAT", margin + contentWidth / 2, 230f, labelPaint)
        canvas.drawText(data.seat, margin + contentWidth / 2, 255f, valuePaint)

        // Row 2: Boarding | Terminal
        canvas.drawText("BOARDING", margin, 285f, labelPaint)
        canvas.drawText(data.boardingTime, margin, 310f, valuePaint)

        canvas.drawText("TERMINAL", margin + contentWidth / 2, 285f, labelPaint)
        canvas.drawText(data.terminal, margin + contentWidth / 2, 310f, valuePaint)

        // Row 3: Dep. | Arr.
        canvas.drawText("DEPARTS", margin, 340f, labelPaint)
        canvas.drawText(data.departureTime, margin, 365f, valuePaint)

        canvas.drawText("ARRIVES", margin + contentWidth / 2, 340f, labelPaint)
        canvas.drawText(data.arrivalTime, margin + contentWidth / 2, 365f, valuePaint)

        // ── Dashed separator ──
        canvas.drawLine(margin, 385f, PAGE_WIDTH - margin, 385f, dashedPaint)

        // ── QR Code ──
        if (data.qrCodeData.isNotBlank()) {
            val qrBitmap = generateQrBitmap(data.qrCodeData, 180)
            if (qrBitmap != null) {
                val qrLeft = (PAGE_WIDTH - 180) / 2f
                canvas.drawBitmap(qrBitmap, qrLeft, 400f, null)
            }
        }

        // ── Passenger footer ──
        val footerBg = Paint().apply { color = lightGray }
        canvas.drawRect(margin, 610f, PAGE_WIDTH - margin, 680f, footerBg)

        val passengerLabelPaint = Paint().apply { color = subtleGray; textSize = 9f; isFakeBoldText = true }
        val passengerNamePaint = Paint().apply { color = navyBlue; textSize = 16f; isFakeBoldText = true }
        canvas.drawText("PASSENGER", margin + 12f, 635f, passengerLabelPaint)
        canvas.drawText(data.passengerName, margin + 12f, 655f, passengerNamePaint)

        val refLabelPaint = Paint().apply {
            color = subtleGray; textSize = 9f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT
        }
        val refValuePaint = Paint().apply {
            color = navyBlue; textSize = 14f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT
        }
        canvas.drawText("BOOKING REF", PAGE_WIDTH - margin - 12f, 635f, refLabelPaint)
        canvas.drawText(data.bookingReference, PAGE_WIDTH - margin - 12f, 655f, refValuePaint)

        // ── Bottom note ──
        val notePaint = Paint().apply { color = subtleGray; textSize = 8f }
        canvas.drawText(
            "Generated by Skywire Airlines • Boarding pass valid only with valid ID",
            margin, 710f, notePaint
        )
    }

    private fun generateQrBitmap(data: String, size: Int): Bitmap? {
        return try {
            val hints = mapOf(EncodeHintType.MARGIN to 1)
            val writer = QRCodeWriter()
            val bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, size, size, hints)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    private fun openOutputStream(context: Context, fileName: String): OutputStream? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val contentValues = ContentValues().apply {
                put(MediaStore.Downloads.DISPLAY_NAME, fileName)
                put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
                put(MediaStore.Downloads.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }
            val uri = context.contentResolver.insert(
                MediaStore.Downloads.EXTERNAL_CONTENT_URI,
                contentValues
            ) ?: return null
            context.contentResolver.openOutputStream(uri)
        } else {
            @Suppress("DEPRECATION")
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            dir.mkdirs()
            java.io.FileOutputStream(java.io.File(dir, fileName))
        }
    }
}