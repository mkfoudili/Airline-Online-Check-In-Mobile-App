package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.local.entity.FlightEntity

@Dao
interface FlightDao {
    @Query("SELECT * FROM flights WHERE flightId = :flightId")
    suspend fun getFlightById(flightId: String): FlightEntity?

    @Query("SELECT * FROM flights")
    suspend fun getAllFlights(): List<FlightEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFlight(entity: FlightEntity)

    @Upsert
    suspend fun insertFlights(entities: List<FlightEntity>)

    @Query("DELETE FROM flights")
    suspend fun deleteAllFlights()
}
