package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

/**
 * DTO returned by POST /api/boarding/generate and GET /api/boarding/my.
 * Field names mirror the server's camelCase JSON response.
 */
data class BoardingPassDto(
    @SerializedName("passId")          val passId: String,
    @SerializedName("passengerId")     val passengerId: String,
    @SerializedName("flightId")        val flightId: String,
    @SerializedName("flightNumber")    val flightNumber: String,
    @SerializedName("origin")          val origin: String,
    @SerializedName("originCity")      val originCity: String,
    @SerializedName("destination")     val destination: String,
    @SerializedName("destinationCity") val destinationCity: String,
    @SerializedName("passengerName")   val passengerName: String,
    @SerializedName("seatNumber")      val seatNumber: String,
    @SerializedName("gate")            val gate: String,
    @SerializedName("boardingTime")    val boardingTime: String,
    @SerializedName("terminal")        val terminal: String?,
    @SerializedName("departureTime")   val departureTime: String,  // ISO-8601
    @SerializedName("arrivalTime")     val arrivalTime: String,    // ISO-8601
    @SerializedName("bookingReference") val bookingReference: String,
    @SerializedName("qrCode")          val qrCode: String,
    @SerializedName("issuedAt")        val issuedAt: String        // ISO-8601
)

/** Wrapper matching the server's { success, data, message } envelope. */
data class BoardingPassResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: BoardingPassDto,
    @SerializedName("message") val message: String?
)