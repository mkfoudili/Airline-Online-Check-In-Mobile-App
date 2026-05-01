package com.example.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
import com.example.data.local.entity.NotificationEntity
import com.example.data.local.entity.PassengerEntity
import com.example.data.local.entity.SeatMapEntity
import com.example.data.local.entity.UserEntity

@Database(
    entities = [
        BoardingPassEntity::class,
        BookingEntity::class,
        CheckInSessionEntity::class,
        FlightEntity::class,
        NotificationEntity::class,
        PassengerEntity::class,
        SeatMapEntity::class,
        UserEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boardingPassDao(): BoardingPassDao
    abstract fun bookingDao(): BookingDao
    abstract fun checkInSessionDao(): CheckInSessionDao
    abstract fun flightDao(): FlightDao
    abstract fun notificationDao(): NotificationDao
    abstract fun seatMapDao(): SeatMapDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "checkin_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}