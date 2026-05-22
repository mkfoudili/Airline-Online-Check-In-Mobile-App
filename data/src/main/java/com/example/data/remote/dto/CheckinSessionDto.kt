package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class CheckinSessionDto(
    @SerializedName("sessionId")          val sessionId: String,
    @SerializedName("passengerId")        val passengerId: String,
    @SerializedName("bookingId")          val bookingId: String,
    @SerializedName("currentStep")        val currentStep: String?,
    @SerializedName("passportScanUrl")    val passportScanUrl: String?    = null,
    @SerializedName("ocrValidation")      val ocrValidation: String?      = null,
    @SerializedName("baggageDeclaration") val baggageDeclaration: String? = null,
    @SerializedName("specialRequests")    val specialRequests: String?    = null,
    @SerializedName("completedAt")        val completedAt: Timestamp?     = null
)

data class CreateSessionRequest(
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("bookingId")   val bookingId: String
)

data class CreateSessionResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data")    val data: CheckinSessionDto
)

data class AdvanceStepRequest(
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("step")        val step: String
)

data class AdvanceStepResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("data")    val data: StepData
)

data class StepData(
    @SerializedName("sessionId")   val sessionId: String,
    @SerializedName("currentStep") val currentStep: String
)