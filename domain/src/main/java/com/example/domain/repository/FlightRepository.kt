package com.example.domain.repository

import com.example.domain.model.Flight

interface FlightRepository {
    suspend fun getFlightById(flightId: String): Result<Flight>
    suspend fun refreshFlightsFromRemote(uid: String): Result<Unit>
}