package com.example.check_in_mobile_app.di

import android.content.Context
import com.example.data.local.dao.BoardingPassDao
import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.FlightDao
import com.example.data.local.dao.PassengerDao
import com.example.data.local.room.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideBookingDao(database: AppDatabase): BookingDao {
        return database.bookingDao()
    }

    @Provides
    fun provideFlightDao(database: AppDatabase): FlightDao {
        return database.flightDao()
    }

    @Provides
    fun providePassengerDao(database: AppDatabase): PassengerDao {
        return database.passengerDao()
    }

    @Provides @Singleton
    fun provideBoardingPassDao(db: AppDatabase): BoardingPassDao {
        return db.boardingPassDao()
    }
}