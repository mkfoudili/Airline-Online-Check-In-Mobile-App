package com.example.check_in_mobile_app.di

import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import com.example.domain.usecase.booking.SearchBookingsUseCase

/**
 * Manual DI container for use cases that don't need the database
 * (they use mock data directly). For database-backed repositories,
 * use [com.example.check_in_mobile_app.BaseApplication].
 */
object AppContainer {

    private val bookingRepository by lazy {
        BookingRepositoryImpl()
    }

    // Booking use cases
    val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase by lazy {
        GetUpcomingBookingsUseCase(bookingRepository)
    }

    val searchBookingsUseCase: SearchBookingsUseCase by lazy {
        SearchBookingsUseCase(bookingRepository)
    }

    // Boarding use cases (stateless - no DB needed)
    val generateQRCodeUseCase: GenerateQRCodeUseCase by lazy {
        GenerateQRCodeUseCase()
    }

    val generatePdfUseCase: GeneratePdfUseCase by lazy {
        GeneratePdfUseCase()
    }
}