package com.example.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.data.local.dao.BoardingPassDao
import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.CheckInSessionDao
import com.example.data.local.dao.FlightDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.dao.SeatMapDao
import com.example.data.local.dao.UserDao
import com.example.data.local.entity.BoardingPassEntity
import com.example.data.local.entity.BookingEntity
import com.example.data.local.entity.CheckInSessionEntity
import com.example.data.local.entity.FlightEntity
import com.example.data.local.entity.SeatMapEntity
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PassengerEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        UserEntity::class,
        FlightEntity::class,
        BookingEntity::class,
        PassengerEntity::class,
        BoardingPassEntity::class,
        CheckInSessionEntity::class,
        SeatMapEntity::class,
        NotificationEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun flightDao(): FlightDao
    abstract fun bookingDao(): BookingDao
    abstract fun boardingPassDao(): BoardingPassDao
    abstract fun seatMapDao(): SeatMapDao
    abstract fun checkInSessionDao(): CheckInSessionDao
    abstract fun notificationDao(): NotificationDao


    companion object {
        const val DATABASE_NAME = "airline_db"
    }
}
