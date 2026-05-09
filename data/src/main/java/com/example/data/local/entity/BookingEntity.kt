package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingId: String,
    val flightId: String,
    val uid: String,
    val pnr: String,
    val bookingRef: String = "",
    val lastName: String,
    val status: String,
    val checkinDeadline: Long?,
    val createdAt: Long?,
    // Timestamp de la dernière synchronisation avec le backend
    val lastSyncedAt: Long = System.currentTimeMillis()
)