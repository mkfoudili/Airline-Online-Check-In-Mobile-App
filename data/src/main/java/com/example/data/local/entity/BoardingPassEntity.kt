package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boarding_passes")
data class BoardingPassEntity(
    @PrimaryKey val passId: String,
    val passengerId: String,
    val flightId: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String,
    val destination: String,
    val destinationCity: String,
    val passengerName: String,
    val seatNumber: String?,
    val gate: String?,
    val boardingTime: String?,
    val departureTime: Long?,
    val arrivalTime: Long?,
    val bookingReference: String,
    val terminal: String?,
    val qrCodeData: String?,
    val issuedAt: Long,
    val isSyncedWithServer: Boolean = false
)