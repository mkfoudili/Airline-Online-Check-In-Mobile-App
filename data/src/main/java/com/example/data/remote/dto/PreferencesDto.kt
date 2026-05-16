package com.example.data.remote.dto

import com.google.gson.annotations.SerializedName
import java.util.Date

data class PreferencesDto(
    @SerializedName("preferenceId") val preferenceId: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("preferredSoutien") val preferredSoutien: Boolean,
    @SerializedName("preferredVisualsAudit") val preferredVisualsAudit: Boolean,
    @SerializedName("preferredChildCare") val preferredChildCare: Boolean,
    @SerializedName("preferredPetCare") val preferredPetCare: Boolean,
    @SerializedName("mealPreference") val mealPreference: Boolean,
    @SerializedName("createdAt") val createdAt: Date?,
    @SerializedName("updatedAt") val updatedAt: Date?
)

data class ConcludeCheckinRequest(
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("uid") val uid: String,
    @SerializedName("preferredSoutien") val preferredSoutien: Boolean,
    @SerializedName("preferredVisualsAudit") val preferredVisualsAudit: Boolean,
    @SerializedName("preferredChildCare") val preferredChildCare: Boolean,
    @SerializedName("preferredPetCare") val preferredPetCare: Boolean,
    @SerializedName("mealPreference") val mealPreference: Boolean
)

data class ConcludeCheckinResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: ConcludeData
)

data class ConcludeData(
    @SerializedName("preferences") val preferences: PreferencesDto,
    @SerializedName("checkinSession") val checkinSession: ConcludeSession
)

data class ConcludeSession(
    @SerializedName("sessionId") val sessionId: String,
    @SerializedName("passengerId") val passengerId: String,
    @SerializedName("currentStep") val currentStep: String,
    @SerializedName("completedAt") val completedAt: Date?
)
