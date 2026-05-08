package com.example.data.remote

import com.example.data.remote.dto.FlightDto
import com.example.data.remote.retrofit.ApiService
import javax.inject.Inject

class FlightDataSource @Inject constructor() {

    suspend fun getFlightById(flightId: String): FlightDto {
        return ApiService.api.getFlight(flightId)
    }
}