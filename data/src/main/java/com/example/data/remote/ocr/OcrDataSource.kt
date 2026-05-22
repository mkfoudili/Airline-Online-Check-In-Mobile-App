package com.example.data.remote.ocr

import android.graphics.Bitmap
import com.example.domain.model.ParsedPassportData
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * Wraps ML Kit Text Recognition to extract and parse passport MRZ data
 * from a captured Bitmap. Runs fully on-device with no network calls.
 */
class OcrDataSource @Inject constructor() {

    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    /**
     * Processes [bitmap] with ML Kit and attempts to parse the passport MRZ.
     * Returns [ParsedPassportData] if MRZ lines are found, null otherwise.
     * This is a suspend function — safe to call from a coroutine.
     */
    suspend fun extractPassportData(bitmap: Bitmap): ParsedPassportData? =
        suspendCancellableCoroutine { continuation ->
            val image = InputImage.fromBitmap(bitmap, 0)

            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    val parsed = MrzParser.parse(visionText.text)
                    continuation.resume(parsed)
                }
                .addOnFailureListener { e ->
                    // Return empty data with the error message in rawText for debugging
                    continuation.resume(
                        ParsedPassportData(null, null, null, null, null, null, "OCR_FAILURE: ${e.message}")
                    )
                }

            continuation.invokeOnCancellation {
                recognizer.close()
            }
        }
}