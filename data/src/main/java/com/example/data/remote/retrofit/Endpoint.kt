package com.example.data.remote.retrofit

import com.example.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
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

    @POST("auth/google")
    suspend fun loginWithGoogle(@Body request: GoogleAuthRequest): AuthResponse

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
    @POST("checkin/baggage")
    suspend fun saveBaggage(@Body body: Map<String, Any>): BaggageResponse

    @GET("checkin/baggage/{passengerId}")
    suspend fun getBaggage(@Path("passengerId") passengerId: String): BaggageResponse

    @GET("auth/profile")
    suspend fun getProfile(@Header("Authorization") token: String): Response<ProfileResponse>

    @PUT("auth/profile")
    suspend fun updateProfile(@Header("Authorization") token: String, @Body request: UpdateProfileRequest): Response<ProfileResponse>

    @PUT("auth/profile/password")
    suspend fun updatePassword(@Header("Authorization") token: String, @Body request: UpdatePasswordRequest): Response<MessageResponse>

    @GET("selectseats/flights/{flightId}/seats")
    suspend fun getSeatMap(@Path("flightId") flightId: String): List<SeatMapDto>

    @POST("selectseats/passengers/{passengerId}/seat")
    suspend fun selectSeat(
        @Path("passengerId") passengerId: String,
        @Body request: SelectSeatRequest
    ): SeatMapDto

    // --- Special Requests & Preferences ---
    @GET("preferences/{uid}")
    suspend fun getUserPreferences(@Path("uid") uid: String): PreferencesDto

    @POST("preferences/conclude")
    suspend fun concludeCheckin(@Body request: ConcludeCheckinRequest): ConcludeCheckinResponse


    // Boarding Pass
    @POST("boarding/generate")
    suspend fun generateBoardingPass(
        @Body body: Map<String, String>
    ): BoardingPassResponse

    @GET("boarding/my")
    suspend fun getMyBoardingPass(): BoardingPassResponse

    @GET("boarding/my/all")
    suspend fun getMyBoardingPasses(): BoardingPassListResponse

    @POST("checkin/baggage")
    suspend fun declareBaggage(@Header("Authorization") token: String, @Body request: BaggageRequest): Response<BaggageResponse>

    // --- Notifications ---

    @POST("notifications/register-token")
    suspend fun registerToken(@Body request: RegisterTokenRequest): Response<Unit>

    @GET("notifications")
    suspend fun getNotifications(): NotificationListResponse

    @PATCH("notifications/{notificationId}/read")
    suspend fun markAsRead(@Path("notificationId") notificationId: String): NotificationResponse

    @PATCH("notifications/read-all")
    suspend fun markAllAsRead(): ReadAllResponse
}
