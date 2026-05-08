package com.example.data.remote.retrofit

import com.example.data.remote.dto.*
import com.example.data.remote.URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.data.remote.dto.BookingDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

/**
 * Retrofit API definitions for authentication and user management.
 * The implementation is provided by Hilt via NetworkModule.
 */
interface Endpoint {
    @GET("bookings")
    suspend fun getBookings(@Query("uid") uid: String): List<BookingDto>

    @GET("bookings/upcoming")
    suspend fun getUpcomingBookings(@Query("uid") uid: String): List<BookingDto>

    @GET("flights/{id}")
    suspend fun getFlight(@Path("id") id: String): FlightDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequest): AuthResponse

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): AuthResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body request: RefreshRequest): TokenResponse

    @GET("auth/exists")
    suspend fun emailExists(@Query("email") email: String): Boolean

    @POST("auth/logout")
    suspend fun logout()
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
