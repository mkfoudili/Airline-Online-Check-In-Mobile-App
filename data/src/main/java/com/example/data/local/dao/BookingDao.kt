package com.example.data.local.dao

import androidx.room.*
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.FlightItineraryEntity
import com.example.data.local.entity.PassengerEntity

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings WHERE pnr = :pnr AND lastName = :lastName")
    suspend fun getBookingByPnrAndLastName(pnr: String, lastName: String): BookingEntity?

    @Transaction
    @Query("SELECT * FROM bookings WHERE pnr = :pnr AND lastName = :lastName")
    suspend fun getFullBookingByPnr(pnr: String, lastName: String): FlightItineraryEntity?

    @Transaction
    @Query("SELECT * FROM bookings WHERE uid = :uid")
    suspend fun getFullBookingsByUid(uid: String): List<FlightItineraryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(entity: BookingEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPassengers(entities: List<PassengerEntity>)

    @Query("DELETE FROM bookings")
    suspend fun deleteAll()
}
