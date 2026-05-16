package com.example.data.remote.retrofit

import com.example.data.remote.dto.*
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

    @GET("bookings/search")
    suspend fun searchBooking(
        @Query("pnr") pnr: String,
        @Query("lastName") lastName: String
    ): BookingDto

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

    @POST("auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): AuthResponse

    @POST("auth/logout")
    suspend fun logout()

    // --- Seat Map ---
    @GET("selectseats/flights/{flightId}/seats")
    suspend fun getSeatMap(@Path("flightId") flightId: String): List<SeatMapDto>

    @POST("selectseats/passengers/{passengerId}/seat")
    suspend fun selectSeat(
        @Path("passengerId") passengerId: String,
        @Body request: SelectSeatRequest
    ): SeatMapDto
}
