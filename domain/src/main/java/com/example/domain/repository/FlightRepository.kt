package com.example.domain.repository

import com.example.domain.model.Flight
import com.example.domain.model.FlightItinerary
import com.example.domain.model.Seat

interface FlightRepository {
    fun getItenary(flightNumber: String, callback: (Result<FlightItinerary>) -> Unit)
    fun getFlightById(flightId: String, callback: (Result<Flight>) -> Unit)
}