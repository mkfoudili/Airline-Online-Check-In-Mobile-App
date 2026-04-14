package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "flights")
data class FlightEntity(
    @PrimaryKey val flightId: String,
    val flightNumber: String,
    val origin: String,
    val destination: String,
    val departureTime: Long?,
    val arrivalTime: Long?,
    val aircraftType: String?,
    val status: String?
)
