package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.SeatMapEntity

@Dao
interface SeatMapDao {
    @Query("SELECT * FROM seats WHERE flightId = :flightId")
    suspend fun getSeatsByFlightId(flightId: String): List<SeatMapEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeats(entities: List<SeatMapEntity>)

    @Query("UPDATE seats SET isAvailable = :isAvailable, occupiedBy = :passengerId WHERE seatId = :seatId")
    suspend fun updateSeatReservation(seatId: String, isAvailable: Boolean, passengerId: String?)

    @Query("DELETE FROM seats WHERE flightId = :flightId")
    suspend fun deleteByFlightId(flightId: String)
}
