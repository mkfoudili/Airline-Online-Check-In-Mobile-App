package com.example.check_in_mobile_app.di

import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import com.example.domain.usecase.booking.SearchBookingsUseCase


object AppContainer {


    private val bookingRepository by lazy { 
        BookingRepositoryImpl() 
    }

    private val checkInRepository by lazy {
        com.example.data.repository.CheckInRepositoryImpl()
    }

    // Use Cases
    val getUpcomingBookingsUseCase: GetUpcomingBookingsUseCase by lazy {
        GetUpcomingBookingsUseCase(bookingRepository)
    }

    val searchBookingsUseCase: SearchBookingsUseCase by lazy {
        SearchBookingsUseCase(bookingRepository)
    }
    
}

