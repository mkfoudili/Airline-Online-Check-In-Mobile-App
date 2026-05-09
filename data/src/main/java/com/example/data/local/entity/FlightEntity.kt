package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class FlightEntity(
    @PrimaryKey val flightId: String,
    val flightNumber: String,
    val origin: String,
    val originCity: String = "",
    val destination: String,
    val destinationCity: String = "",
    val departureTime: Long?,
    val arrivalTime: Long?,
    val aircraftType: String?,
    val status: String?,
    val gate: String? = null,
    val terminal: String? = null,
    val boardingTime: String? = null,
    val checkInOpensTime: String? = null,
    // Timestamp de la dernière synchronisation avec le backend
    val lastSyncedAt: Long = System.currentTimeMillis()
)