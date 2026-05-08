package com.example.data.remote

import com.example.data.remote.dto.FlightDto
import java.util.*
import javax.inject.Inject

class FlightDataSource @Inject constructor() {

    /**
     * Fetches the itinerary for a given flight number.
     * Simulated to return a static flight.
     */
    fun getItinerary(flightNumber: String, callback: (Result<FlightDto>) -> Unit) {
        val flight = FlightDto(
            flightId = "F101",
            flightNumber = flightNumber,
            origin = "LHR",
            destination = "CDG",
            departureTime = Date(System.currentTimeMillis() + 7200000), // 2 hours from now
            arrivalTime = Date(System.currentTimeMillis() + 10800000), // 3 hours from now
            aircraftType = "Boeing 737",
            status = "Scheduled"
        )
        callback(Result.success(flight))
    }

    /**
     * Fetches a flight by its ID.
     * Simulated to return a static flight.
     */
    fun getFlightById(flightId: String, callback: (Result<FlightDto>) -> Unit) {
        val flight = FlightDto(
            flightId = flightId,
            flightNumber = "SW402",
            origin = "LHR",
            destination = "CDG",
            departureTime = Date(System.currentTimeMillis() + 7200000),
            arrivalTime = Date(System.currentTimeMillis() + 10800000),
            aircraftType = "Airbus A320",
            status = "On Time"
        )
        callback(Result.success(flight))
    }
}