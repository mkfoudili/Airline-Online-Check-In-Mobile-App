package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "boarding_passes")
data class BoardingPassEntity(
    @PrimaryKey val passId: String,
    val passengerId: String,
    val flightId: String,
    val qrCode: String?,
    val seatNumber: String?,
    val gate: String?,
    val boardingTime: String?,
    val issuedAt: Long?
)
