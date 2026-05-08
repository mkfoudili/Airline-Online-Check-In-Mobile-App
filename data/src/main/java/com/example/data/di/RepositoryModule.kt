package com.example.data.di

import com.example.data.repository.AuthRepositoryImpl
import com.example.data.repository.BoardingPassRepositoryImpl
import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.repository.AuthRepository
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.repository.BookingRepository
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
}
