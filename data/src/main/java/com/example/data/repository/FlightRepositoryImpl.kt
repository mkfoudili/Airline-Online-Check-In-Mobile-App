package com.example.data.repository

import com.example.data.local.dao.FlightDao
import com.example.data.mapper.toDomain
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.FlightItinerary
import com.example.domain.repository.FlightRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FlightRepositoryImpl(
    private val flightDataSource: FlightDataSource,
    private val flightDao: FlightDao
) : FlightRepository {

    override fun getFlightById(flightId: String, callback: (Result<Flight>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localFlight = flightDao.getFlightById(flightId)
                if (localFlight != null) {
                    callback(Result.success(localFlight.toDomain()))
                } else {
                    //Cache local
                    flightDataSource.getFlightById(flightId) { result ->
                        result.onSuccess { flightDto ->
                            callback(Result.success(flightDto.toDomain()))
                        }.onFailure {
                            callback(Result.failure(it))
                        }
                    }
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun getItenary(flightNumber: String, callback: (Result<FlightItinerary>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localItinerary = flightDao.getItinerary(flightNumber)
                if (localItinerary != null) {
                    callback(Result.success(localItinerary.toDomain()))
                } else {
                    flightDataSource.getItinerary(flightNumber) { result ->
                        result.onSuccess { flightDto ->
                            val domainFlight = flightDto.toDomain()
                            val itinerary = FlightItinerary(
                                booking = Booking(
                                    bookingId = "MOCK_ID",
                                    pnr = "MOCK_PNR",
                                    lastName = "MOCK_NAME",
                                    status = CheckInStatus.CONFIRMED,
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
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }
}
