package com.example.data.remote.retrofit

import com.example.data.remote.dto.*
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface Endpoint {

    // Bookings
    @GET("bookings")
    suspend fun getBookings(): List<BookingDto>

    @GET("bookings/upcoming")
    suspend fun getUpcomingBookings(): List<BookingDto>

    @GET("bookings/search")
    suspend fun searchBooking(
        @Query("pnr") pnr: String,
        @Query("lastName") lastName: String
    ): BookingDto

    // Flights
    @GET("flights/{id}")
    suspend fun getFlight(@Path("id") id: String): FlightDto

    // Auth
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

    // Check-in : Session
    @POST("checkin/session")
    suspend fun createOrResumeSession(
        @Body request: CreateSessionRequest
    ): CreateSessionResponse

    @PATCH("checkin/session/step")
    suspend fun advanceSessionStep(
        @Body request: AdvanceStepRequest
    ): AdvanceStepResponse

    // Check-in : Passport
    @GET("checkin/verify-passport")
    suspend fun verifyPassport(
        @Query("passportNumber") passportNumber: String,
        @Query("lastName") lastName: String,
        @Query("firstName") firstName: String? = null,
        @Query("nationality") nationality: String? = null,
        @Query("dateOfBirth") dateOfBirth: String? = null,
        @Query("expiryDate") expiryDate: String? = null
    ): VerifyPassportResponseDto

    // Check-in : Baggage
    /*@POST("checkin/baggage")
    suspend fun saveBaggage(@Body body: Map<String, Any>): BaggageResponse

    @GET("checkin/baggage/{passengerId}")
    suspend fun getBaggage(@Path("passengerId") passengerId: String): BaggageResponse*/

    // Boarding Pass
    @POST("boarding/generate")
    suspend fun generateBoardingPass(
        @Body body: Map<String, String>
    ): BoardingPassResponse

    @GET("boarding/my")
    suspend fun getMyBoardingPass(): BoardingPassResponse
}