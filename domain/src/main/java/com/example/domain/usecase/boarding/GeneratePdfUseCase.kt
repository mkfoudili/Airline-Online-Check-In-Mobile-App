package com.example.domain.usecase.boarding

import javax.inject.Inject

import com.example.domain.model.BoardingPass
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GeneratePdfUseCase @Inject constructor() {

    operator fun invoke(boardingPass: BoardingPass): PdfBoardingPassData {
        val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
        val timeFormatter = SimpleDateFormat("HH:mm", Locale.ENGLISH)

        val departureDate = boardingPass.departureTime?.let {
            dateFormatter.format(Date(it))
        } ?: "N/A"

        val departureTime = boardingPass.departureTime?.let {
            timeFormatter.format(Date(it))
        } ?: "N/A"

        val arrivalTime = boardingPass.arrivalTime?.let {
            timeFormatter.format(Date(it))
        } ?: "N/A"

        return PdfBoardingPassData(
            passengerName = boardingPass.passengerName,
            flightNumber = boardingPass.flightNumber,
            origin = boardingPass.origin,
            originCity = boardingPass.originCity,
            destination = boardingPass.destination,
            destinationCity = boardingPass.destinationCity,
            departureDate = departureDate,
            departureTime = departureTime,
            arrivalTime = arrivalTime,
            gate = boardingPass.gate ?: "TBD",
            seat = boardingPass.seatNumber ?: "N/A",
            boardingTime = boardingPass.boardingTime ?: "N/A",
            terminal = boardingPass.terminal ?: "N/A",
            bookingReference = boardingPass.bookingReference,
            qrCodeData = boardingPass.qrCodeData ?: ""
        )
    }
}

data class PdfBoardingPassData(
    val passengerName: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String,
    val destination: String,
    val destinationCity: String,
    val departureDate: String,
    val departureTime: String,
    val arrivalTime: String,
    val gate: String,
    val seat: String,
    val boardingTime: String,
    val terminal: String,
    val bookingReference: String,
    val qrCodeData: String
)