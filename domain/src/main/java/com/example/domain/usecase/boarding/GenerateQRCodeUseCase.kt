package com.example.domain.usecase.boarding

import javax.inject.Inject

import com.example.domain.model.BoardingPass

class GenerateQRCodeUseCase @Inject constructor() {

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