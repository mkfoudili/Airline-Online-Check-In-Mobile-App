package com.example.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class FlightItineraryEntity(
    @Embedded val booking: BookingEntity,
    
    @Relation(
        parentColumn = "flightId",
        entityColumn = "flightId"
    )
    val flight: FlightEntity,
    
    @Relation(
        parentColumn = "bookingId",
        entityColumn = "bookingId"
    )
    val passengers: List<PassengerEntity>
)
//to fix