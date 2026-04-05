package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seats")
data class SeatMapEntity(
    @PrimaryKey val seatId: String,
    val flightId: String,
    val seatNumber: String,
    val isAvailable: Boolean,
    val cabinClass: String?
)
// + seatClass + isPremium + occupiedBy
// - cabinClass