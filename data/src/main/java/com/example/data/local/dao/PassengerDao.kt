package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.PassengerEntity

@Dao
interface PassengerDao {

    @Query("SELECT * FROM passengers WHERE bookingId = :bookingId")
    suspend fun getPassengersByBookingId(bookingId: String): List<PassengerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassengers(entities: List<PassengerEntity>)

    @Query("DELETE FROM passengers WHERE bookingId = :bookingId")
    suspend fun deleteByBookingId(bookingId: String)

    @Query("DELETE FROM passengers")
    suspend fun deleteAll()
}