package com.example.data.remote.retrofit

import com.example.data.remote.dto.*
import com.example.data.remote.URL
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * Retrofit API definitions for authentication and user management.
 * The implementation is provided by Hilt via NetworkModule.
 */
interface Endpoint {
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
