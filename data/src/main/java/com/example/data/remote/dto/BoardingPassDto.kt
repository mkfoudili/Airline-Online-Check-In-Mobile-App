package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName

data class BoardingPassDto(
    @SerializedName("passId")          val passId: String,
    @SerializedName("passengerId")     val passengerId: String,
    @SerializedName("uid")             val uid: String? = null,
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
    @SerializedName("departureTime")   val departureTime: String,
    @SerializedName("arrivalTime")     val arrivalTime: String,
    @SerializedName("bookingReference") val bookingReference: String,
    @SerializedName("qrCode")          val qrCode: String,
    @SerializedName("issuedAt")        val issuedAt: String
)

/** Wrapper pour POST /boarding/generate et GET /boarding/my — objet unique. */
data class BoardingPassResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: BoardingPassDto,
    @SerializedName("message") val message: String?
)

/** Wrapper pour GET /boarding/my/all — retourne une liste. */
data class BoardingPassListResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: List<BoardingPassDto>,
    @SerializedName("message") val message: String?
)