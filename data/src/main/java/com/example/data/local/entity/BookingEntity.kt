package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Booking
import com.example.domain.model.Flight

@Entity(tableName = "bookings")
data class BookingEntity(
    @PrimaryKey val bookingId: String,
    val pnr: String,
    val lastName: String,
    val status: String,
    val flightId: String
)

// + checkinDeadline +uid
// - flightId