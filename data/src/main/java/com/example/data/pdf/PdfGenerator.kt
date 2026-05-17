package com.example.data.pdf

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.core.content.FileProvider
import com.example.domain.usecase.boarding.PdfBoardingPassData
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.qrcode.QRCodeWriter
import java.io.File
import java.io.OutputStream

/**
 * Generates a PDF boarding pass and saves it to the Downloads folder.
 *
 * Returns a [PdfResult] containing:
 *  - the file name
 *  - the content [Uri] so the caller can launch an "Open with" chooser
 */
object PdfGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842

    data class PdfResult(
        val fileName: String,
        val uri: Uri
    )

    /**
     * Generates the PDF, writes it to Downloads, and returns a [Result<PdfResult>].
     * On success, call [openWithChooser] to show the "Open with" bottom sheet.
     */
    fun generate(context: Context, data: PdfBoardingPassData): Result<PdfResult> {
        return try {
            val document = PdfDocument()
            val pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, 1).create()
            val page = document.startPage(pageInfo)
            drawBoardingPass(page.canvas, data)
            document.finishPage(page)

            val fileName = "boarding_pass_${data.bookingReference}.pdf"
            val (outputStream, uri) = openOutputStream(context, fileName)
                ?: return Result.failure(Exception("Cannot open output stream"))

            outputStream.use { document.writeTo(it) }
            document.close()

            Result.success(PdfResult(fileName = fileName, uri = uri))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Launches the Android "Open with" chooser (the bottom sheet that shows Drive, etc.).
     * Call this after a successful [generate].
     */
    fun openWithChooser(context: Context, uri: Uri) {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        val chooser = Intent.createChooser(intent, "Open boarding pass with")
        chooser.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(chooser)
    }

    // Private helpers

    private fun drawBoardingPass(canvas: Canvas, data: PdfBoardingPassData) {
        val navyBlue = Color.rgb(26, 54, 93)
        val lightGray = Color.rgb(248, 250, 252)
        val subtleGray = Color.rgb(100, 116, 139)
        val white = Color.WHITE
        val margin = 40f
        val contentWidth = PAGE_WIDTH - 2 * margin

        // Header background
        canvas.drawRect(margin, 40f, PAGE_WIDTH - margin, 110f,
            Paint().apply { color = navyBlue })

        // Header: airline name
        canvas.drawText("SKYWIRE AIRLINES", margin + 16f, 70f,
            Paint().apply { color = white; textSize = 14f; isFakeBoldText = true })
        canvas.drawText("FLIGHT ${data.flightNumber}", PAGE_WIDTH - margin - 90f, 70f,
            Paint().apply { color = Color.argb(200, 255, 255, 255); textSize = 11f })
        canvas.drawText(data.departureDate, margin + 16f, 92f,
            Paint().apply { color = Color.argb(180, 255, 255, 255); textSize = 10f })

        // Route
        canvas.drawText(data.origin, margin, 165f,
            Paint().apply { color = navyBlue; textSize = 36f; isFakeBoldText = true })
        canvas.drawText(data.originCity.uppercase(), margin, 180f,
            Paint().apply { color = subtleGray; textSize = 10f })

        val centerX = PAGE_WIDTH / 2f
        val arrowPaint = Paint().apply { color = subtleGray; strokeWidth = 2f; style = Paint.Style.STROKE }
        canvas.drawLine(margin + 80f, 155f, centerX - 20f, 155f, arrowPaint)
        canvas.drawLine(centerX + 20f, 155f, PAGE_WIDTH - margin - 80f, 155f, arrowPaint)
        canvas.drawText("✈", centerX - 10f, 162f,
            Paint().apply { color = navyBlue; textSize = 18f; isFakeBoldText = true })

        canvas.drawText(data.destination, PAGE_WIDTH - margin, 165f,
            Paint().apply { color = navyBlue; textSize = 36f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT })
        canvas.drawText(data.destinationCity.uppercase(), PAGE_WIDTH - margin, 180f,
            Paint().apply { color = subtleGray; textSize = 10f; textAlign = Paint.Align.RIGHT })

        // Dashed separator
        val dashedPaint = Paint().apply {
            color = Color.rgb(203, 213, 225); strokeWidth = 1.5f; style = Paint.Style.STROKE
            pathEffect = android.graphics.DashPathEffect(floatArrayOf(10f, 6f), 0f)
        }
        canvas.drawLine(margin, 200f, PAGE_WIDTH - margin, 200f, dashedPaint)

        val labelPaint = Paint().apply { color = subtleGray; textSize = 9f }
        val valuePaint = Paint().apply { color = navyBlue; textSize = 22f; isFakeBoldText = true }

        canvas.drawText("GATE", margin, 230f, labelPaint)
        canvas.drawText(data.gate, margin, 255f, valuePaint)
        canvas.drawText("SEAT", margin + contentWidth / 2, 230f, labelPaint)
        canvas.drawText(data.seat, margin + contentWidth / 2, 255f, valuePaint)

        canvas.drawText("BOARDING", margin, 285f, labelPaint)
        canvas.drawText(data.boardingTime, margin, 310f, valuePaint)
        canvas.drawText("TERMINAL", margin + contentWidth / 2, 285f, labelPaint)
        canvas.drawText(data.terminal, margin + contentWidth / 2, 310f, valuePaint)

        canvas.drawText("DEPARTS", margin, 340f, labelPaint)
        canvas.drawText(data.departureTime, margin, 365f, valuePaint)
        canvas.drawText("ARRIVES", margin + contentWidth / 2, 340f, labelPaint)
        canvas.drawText(data.arrivalTime, margin + contentWidth / 2, 365f, valuePaint)

        canvas.drawLine(margin, 385f, PAGE_WIDTH - margin, 385f, dashedPaint)

        // QR Code
        if (data.qrCodeData.isNotBlank()) {
            generateQrBitmap(data.qrCodeData, 180)?.let { qrBitmap ->
                canvas.drawBitmap(qrBitmap, (PAGE_WIDTH - 180) / 2f, 400f, null)
            }
        }

        // Passenger footer
        canvas.drawRect(margin, 610f, PAGE_WIDTH - margin, 680f,
            Paint().apply { color = lightGray })

        canvas.drawText("PASSENGER", margin + 12f, 635f,
            Paint().apply { color = subtleGray; textSize = 9f; isFakeBoldText = true })
        canvas.drawText(data.passengerName, margin + 12f, 655f,
            Paint().apply { color = navyBlue; textSize = 16f; isFakeBoldText = true })

        canvas.drawText("BOOKING REF", PAGE_WIDTH - margin - 12f, 635f,
            Paint().apply { color = subtleGray; textSize = 9f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT })
        canvas.drawText(data.bookingReference, PAGE_WIDTH - margin - 12f, 655f,
            Paint().apply { color = navyBlue; textSize = 14f; isFakeBoldText = true; textAlign = Paint.Align.RIGHT })

        canvas.drawText("Generated by Skywire Airlines • Boarding pass valid only with valid ID",
            margin, 710f, Paint().apply { color = subtleGray; textSize = 8f })
    }

    private fun generateQrBitmap(data: String, size: Int): Bitmap? {
        return try {
            val hints = mapOf(EncodeHintType.MARGIN to 1)
            val bitMatrix = QRCodeWriter().encode(data, BarcodeFormat.QR_CODE, size, size, hints)
            val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
            for (x in 0 until size) {
                for (y in 0 until size) {
                    bitmap.setPixel(x, y, if (bitMatrix[x, y]) Color.BLACK else Color.WHITE)
                }
            }
            bitmap
        } catch (_: Exception) {
            null
        }
    }

    /**
     * Opens an OutputStream for the PDF file and returns it alongside its content Uri.
     */
    private fun openOutputStream(context: Context, fileName: String): Pair<OutputStream, Uri>? {
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
            val stream = context.contentResolver.openOutputStream(uri) ?: return null
            Pair(stream, uri)
        } else {
            @Suppress("DEPRECATION")
            val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            dir.mkdirs()
            val file = File(dir, fileName)
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            Pair(java.io.FileOutputStream(file), uri)
        }
    }
}