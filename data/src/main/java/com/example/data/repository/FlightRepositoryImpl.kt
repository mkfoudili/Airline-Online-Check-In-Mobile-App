package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
import com.example.domain.model.Flight
import com.example.domain.model.FlightItinerary
import com.example.domain.repository.FlightRepository

class FlightRepositoryImpl (private val flightDataSource: FlightDataSource) : FlightRepository {

    override fun getItenary(flightNumber: String, callback: (Result<FlightItinerary>) -> Unit) {
        flightDataSource.getItinerary(flightNumber) { result ->
            result.onSuccess { flightDto ->
                // In a real app, you would fetch booking and passengers here too
                // For this implementation, we map what we have
                val domainFlight = flightDto.toDomain()
                val itinerary = FlightItinerary(
                    booking = Booking(
                        bookingId = "MOCK_ID",
                        pnr = "MOCK_PNR",
                        lastName = "MOCK_NAME",
                        status = "CONFIRMED",
                        flight = domainFlight,
                        passengers = emptyList()
                    ),
                    flight = domainFlight,
                    passengers = emptyList(),
                    checkInOpen = true,
                    checkInDeadline = domainFlight.departureTime - 3600000 // 1 hour before
                )
                callback(Result.success(itinerary))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun getFlightById(flightId: String, callback: (Result<Flight>) -> Unit) {
        flightDataSource.getFlightById(flightId) { result ->
            result.onSuccess { flightDto ->
                callback(Result.success(flightDto.toDomain()))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }
}
