package com.example.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.preferences.UserPreferencesRepository
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import com.example.data.remote.retrofit.Endpoint
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_prefs")

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    @Singleton
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
        return context.dataStore
    }

    @Provides
    @Singleton
    fun provideUserPreferencesRepository(dataStore: DataStore<Preferences>): UserPreferencesRepository {
        return UserPreferencesRepository(dataStore)
    }

    @Provides
    @Singleton
    fun provideBookingDataSource(endpoint: Endpoint): BookingDataSource {
        return BookingDataSource(endpoint)
    }

    @Provides
    @Singleton
    fun provideFlightDataSource(endpoint: Endpoint): FlightDataSource {
        return FlightDataSource(endpoint)
    }
}