package com.example.data.di

import com.example.data.qr.ZxingQrEncoder
import com.example.data.repository.*
import com.example.domain.repository.*
import com.example.domain.usecase.boarding.QrEncoder
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds @Singleton
    abstract fun bindAuthRepository(impl: AuthRepositoryImpl): AuthRepository

    @Binds @Singleton
    abstract fun bindBoardingPassRepository(impl: BoardingPassRepositoryImpl): BoardingPassRepository

    @Binds @Singleton
    abstract fun bindBookingRepository(impl: BookingRepositoryImpl): BookingRepository

    @Binds @Singleton
    abstract fun bindFlightRepository(impl: FlightRepositoryImpl): FlightRepository

    @Binds @Singleton
    abstract fun bindCheckInRepository(impl: CheckInRepositoryImpl): CheckInRepository

    @Binds @Singleton
    abstract fun bindNotificationRepository(impl: NotificationRepositoryImpl): NotificationRepository

    @Binds @Singleton
    abstract fun bindSeatRepository(impl: SeatRepositoryImpl): SeatRepository

    @Binds @Singleton
    abstract fun bindPassportOcrRepository(impl: PassportOcrRepositoryImpl): PassportOcrRepository
}
    abstract fun bindQrEncoder(impl: ZxingQrEncoder): QrEncoder
}
