package com.example.check_in_mobile_app

import android.app.Application
import com.example.data.local.room.AppDatabase
import com.example.data.repository.BoardingPassRepositoryImpl
import com.example.data.repository.BookingRepositoryImpl
import com.example.data.repository.CheckInRepositoryImpl
import com.example.data.repository.NotificationRepositoryImpl
import com.example.domain.repository.BoardingPassRepository
import com.example.domain.repository.BookingRepository
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BaseApplication : Application() {
    lateinit var database: AppDatabase
        private set

    // Repositories
    lateinit var boardingPassRepository: BoardingPassRepository
        private set
    lateinit var bookingRepository: BookingRepository
        private set

    // Use cases
    lateinit var generateQRCodeUseCase: GenerateQRCodeUseCase
        private set
    lateinit var generatePdfUseCase: GeneratePdfUseCase
        private set

    override fun onCreate() {
        super.onCreate()

        // 1. Build the Room database
        database = AppDatabase.getDatabase(this)

        // 2. Wire up repositories
        boardingPassRepository = BoardingPassRepositoryImpl(
            boardingPassDao = database.boardingPassDao()
        )

        bookingRepository = BookingRepositoryImpl(
            bookingDao = database.bookingDao(),
            flightDao = database.flightDao()
        )

        // 3. Wire up use cases
        generateQRCodeUseCase = GenerateQRCodeUseCase()
        generatePdfUseCase = GeneratePdfUseCase()

        // 4. Seed mock data so the app always works offline
        CoroutineScope(Dispatchers.IO).launch {
            boardingPassRepository.seedMockDataIfEmpty()
        }
    }
}