package com.example.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.local.dao.BoardingPassDao
import com.example.data.local.dao.FlightDao
import com.example.data.local.dao.NotificationDao
import com.example.data.local.entity.BoardingPassEntity
import com.example.data.local.entity.FlightEntity
import com.example.data.local.entity.NotificationEntity

@Database(
    entities = [
        BoardingPassEntity::class,
        FlightEntity::class,
        NotificationEntity::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boardingPassDao(): BoardingPassDao
    abstract fun flightDao(): FlightDao
    abstract fun notificationDao(): NotificationDao

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
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}