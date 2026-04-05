package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.BookingEntity

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE pnr = :pnr")
    suspend fun getBookingByPnr(pnr: String): BookingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(entity: BookingEntity)

    @Query("DELETE FROM bookings")
    suspend fun deleteAll()
}
