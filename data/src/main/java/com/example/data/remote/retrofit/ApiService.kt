package com.example.data.remote.retrofit

import com.example.data.remote.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

import com.example.data.remote.dto.BookingDto
import retrofit2.http.GET
import retrofit2.http.Query

interface Endpoint {
    @GET("bookings")
    suspend fun getBookings(@Query("uid") uid: String): List<BookingDto>

    @GET("bookings/upcoming")
    suspend fun getUpcomingBookings(@Query("uid") uid: String): List<BookingDto>

    @retrofit2.http.GET("flights/{id}")
    suspend fun getFlight(@retrofit2.http.Path("id") id: String): com.example.data.remote.dto.FlightDto
}

object ApiService {
    val api: Endpoint by lazy {
        Retrofit.Builder()
            .baseUrl(URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Endpoint::class.java)
    }
}