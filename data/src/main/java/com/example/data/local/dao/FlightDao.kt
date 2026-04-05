package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.data.local.entity.FlightEntity
import com.example.data.local.entity.FlightItineraryEntity
import com.example.domain.model.Flight

@Dao
interface FlightDao {
    @Query("SELECT * FROM flights WHERE flightId = :flightIdEntity")
    suspend fun getFlightById(flightIdEntity: String): Flight?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlight(entity: FlightEntity)

    @Query("DELETE FROM flights")
    suspend fun deleteAll()
}