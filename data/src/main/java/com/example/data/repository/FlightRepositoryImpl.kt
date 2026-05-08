package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Flight
import com.example.domain.repository.FlightRepository
import javax.inject.Inject

class FlightRepositoryImpl @Inject constructor(
    private val flightDataSource: FlightDataSource
) : FlightRepository {

    override suspend fun getFlightById(flightId: String): Result<Flight> {
        return try {
            val dto = flightDataSource.getFlightById(flightId)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
