package com.example.check_in_mobile_app

import android.app.Application
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.data.local.room.AppDatabase
import com.example.data.repository.BoardingPassRepositoryImpl
import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.usecase.boarding.GenerateQRCodeBitmapUseCase
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.repository.BookingRepository
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import dagger.hilt.android.HiltAndroidApp

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@HiltAndroidApp
class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}