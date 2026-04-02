package com.example.domain.model

data class Booking(
    val bookingRef: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String = "",        
    val destination: String,
    val destinationCity: String = "",   
    val departureDate: String,
    val departureTime: String,
    val duration: String = "",          
    val passengerName: String = "Djerfi Fatma",
    val pnr: String = "BB9XC2",
    val checkInOpensTime: String = "06:15",
    val boardingTime: String = "08:00",
    val gate: String = "G24",
    val status: CheckInStatus
)
