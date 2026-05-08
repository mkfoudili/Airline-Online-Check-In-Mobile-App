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

    lateinit var database: AppDatabase
        private set

    lateinit var networkMonitor: NetworkMonitor
        private set

    lateinit var boardingPassRepository: BoardingPassRepository
        private set

    lateinit var bookingRepository: BookingRepository
        private set

    lateinit var generateQRCodeUseCase: GenerateQRCodeUseCase
        private set

    // Now imported from data.usecase (ZXing lives in :data, not :domain)
    lateinit var generateQRCodeBitmapUseCase: GenerateQRCodeBitmapUseCase
        private set

    lateinit var generatePdfUseCase: GeneratePdfUseCase
        private set

    override fun onCreate() {
        super.onCreate()

        database = AppDatabase.getDatabase(this)
        networkMonitor = NetworkMonitor(this)

        boardingPassRepository = BoardingPassRepositoryImpl(
            boardingPassDao = database.boardingPassDao()
        )
        bookingRepository = BookingRepositoryImpl(
            bookingDataSource = BookingDataSource(),
            flightDataSource = FlightDataSource(),
            bookingDao = database.bookingDao(),
            flightDao = database.flightDao()
        )


        generateQRCodeUseCase = GenerateQRCodeUseCase()
        generateQRCodeBitmapUseCase = GenerateQRCodeBitmapUseCase()
        generatePdfUseCase = GeneratePdfUseCase()

        CoroutineScope(Dispatchers.IO).launch {
            boardingPassRepository.seedMockDataIfEmpty()
        }
    }
}