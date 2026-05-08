package com.example.check_in_mobile_app.di

import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.usecase.boarding.GeneratePdfUseCase
import com.example.domain.usecase.boarding.GenerateQRCodeUseCase
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import com.example.domain.usecase.booking.SearchBookingsUseCase
import com.example.domain.repository.AuthRepository
import com.example.domain.model.User
import com.example.domain.validation.RegistrationRequest
import com.example.data.remote.BookingDataSource

/**
 * Manual DI container for use cases that don't need the database
 * (they use mock data directly). For database-backed repositories,
 * use [com.example.check_in_mobile_app.BaseApplication].
 */
object AppContainer {

    private val authRepository = object : AuthRepository {
        override fun register(request: RegistrationRequest, callback: (Result<User>) -> Unit) {}
        override fun login(email: String, password: String, callback: (Result<User>) -> Unit) {}
        override fun emailExists(email: String, callback: (Result<Boolean>) -> Unit) {}
        override fun logout(callback: () -> Unit) {}
        override fun getCurrentUserId(): String? = "user-fatma-001"
    }

    private val bookingRepository by lazy {
        BookingRepositoryImpl(BookingDataSource(), null, null, null)
    }


    // Booking use cases
    val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase by lazy {
        GetUpcomingBookingsUseCase(bookingRepository, authRepository)
    }

    val searchBookingsUseCase: SearchBookingsUseCase by lazy {
        SearchBookingsUseCase(bookingRepository, authRepository)
    }

    // Boarding use cases (stateless - no DB needed)
    val generateQRCodeUseCase: GenerateQRCodeUseCase by lazy {
        GenerateQRCodeUseCase()
    }

    val generatePdfUseCase: GeneratePdfUseCase by lazy {
        GeneratePdfUseCase()
    }
}