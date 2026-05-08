package com.example.check_in_mobile_app.di

import android.content.Context
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkMonitor(@ApplicationContext context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }

    @Provides
    @Singleton
    fun provideBookingDataSource(): BookingDataSource {
        return BookingDataSource()
    }

    @Provides
    @Singleton
    fun provideFlightDataSource(): FlightDataSource {
        return FlightDataSource()
    }
}