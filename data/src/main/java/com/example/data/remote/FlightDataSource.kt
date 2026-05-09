package com.example.data.remote

import com.example.data.remote.dto.FlightDto
import com.example.data.remote.retrofit.Endpoint
import javax.inject.Inject

class FlightDataSource @Inject constructor(
    private val endpoint: Endpoint
) {
    suspend fun getFlightById(flightId: String): FlightDto {
        return endpoint.getFlight(flightId)
    }
}