package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.domain.model.Passenger

@Entity(tableName = "passengers")
data class PassengerEntity(
    @PrimaryKey val passengerId: String,
    val bookingId: String,
    val uid: String?,
    val firstName: String,
    val lastName: String,
    val passportNumber: String?,
    val nationality: String?,
    val dateOfBirth: String?,
    val seatNumber: String?,
    val checkinStatus: String?
)
