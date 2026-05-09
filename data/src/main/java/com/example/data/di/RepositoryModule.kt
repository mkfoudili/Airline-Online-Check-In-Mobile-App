package com.example.data.di

import com.example.data.repository.*
import com.example.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindBoardingPassRepository(
        boardingPassRepositoryImpl: BoardingPassRepositoryImpl
    ): BoardingPassRepository

    @Binds
    @Singleton
    abstract fun bindBookingRepository(
        bookingRepositoryImpl: BookingRepositoryImpl
    ): BookingRepository

    @Binds
    @Singleton
    abstract fun bindFlightRepository(
        flightRepositoryImpl: FlightRepositoryImpl
    ): FlightRepository

    @Binds
    @Singleton
    abstract fun bindCheckInRepository(
        checkInRepositoryImpl: CheckInRepositoryImpl
    ): CheckInRepository

    @Binds
    @Singleton
    abstract fun bindNotificationRepository(
        notificationRepositoryImpl: NotificationRepositoryImpl
    ): NotificationRepository

    @Binds
    @Singleton
    abstract fun bindSeatRepository(
        seatRepositoryImpl: SeatRepositoryImpl
    ): SeatRepository
}
