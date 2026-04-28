package com.example.domain.usecase.boarding

import com.example.domain.model.BoardingPass

/**
 * Generates the QR code data string for a boarding pass.
 * The actual bitmap rendering is done in the presentation layer (ZXing).
 * This use case returns the raw data to encode.
 */
class GenerateQRCodeUseCase {

    /**
     * Returns the QR code payload string for the given boarding pass.
     * Format: BOARDING:<flightNumber>:<bookingRef>:<seat>:<origin>-<destination>
     */
    operator fun invoke(boardingPass: BoardingPass): String {
        // If the pass already has a QR code data string, use it
        if (!boardingPass.qrCodeData.isNullOrBlank()) {
            return boardingPass.qrCodeData
        }
        // Otherwise build one from the boarding pass fields
        return buildString {
            append("BOARDING:")
            append(boardingPass.flightNumber.replace(" ", ""))
            append(":")
            append(boardingPass.bookingReference)
            append(":")
            append(boardingPass.seatNumber ?: "NA")
            append(":")
            append(boardingPass.origin)
            append("-")
            append(boardingPass.destination)
        }
    }
}