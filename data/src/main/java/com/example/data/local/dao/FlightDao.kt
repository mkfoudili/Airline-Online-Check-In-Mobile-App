package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.FlightEntity
import com.example.data.local.entity.FlightItineraryEntity

@Dao
interface FlightDao {
    @Query("SELECT * FROM flights WHERE flightId = :flightId")
    suspend fun getFlightById(flightId: String): FlightEntity?

    @Transaction
    @Query("SELECT * FROM bookings WHERE flightId IN (SELECT flightId FROM flights WHERE flightNumber = :flightNumber)")
    suspend fun getItinerary(flightNumber: String): FlightItineraryEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlight(entity: FlightEntity)

    @Query("DELETE FROM flights")
    suspend fun deleteAll()
}
