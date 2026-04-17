package com.example.domain.model

data class BoardingPass(
    val passengerId: String,
    val flightNumber: String,
    val origin: String,
    val destination: String,
    val seatNumber: String?,
    val gate: String?,
    val boardingTime: String?,
    val qrCode: String?,
    val pdfUrl: String?
)
