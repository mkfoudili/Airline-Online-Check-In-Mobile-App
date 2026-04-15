package com.example.data.repository

import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.FlightDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.BookingDataSource
import com.example.data.remote.dto.BookingDto
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.repository.BookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingRepositoryImpl(
    private val bookingDataSource: BookingDataSource? = null,
    private val flightDataSource: FlightDataSource? = null,
    private val bookingDao: BookingDao? = null,
    private val flightDao: FlightDao? = null
) : BookingRepository {

    override fun getUpcomingBookings(): List<Booking> {
        val mockFlight = com.example.domain.model.Flight(
            flightId = "f1",
            flightNumber = "SW402",
            origin = "LHR",
            originCity = "London",
            destination = "CDG",
            destinationCity = "Paris",
            departureTime = System.currentTimeMillis() + 86400000,
            arrivalTime = System.currentTimeMillis() + 90000000,
            checkInOpensTime = "06:15",
            boardingTime = "08:00",
            aircraftType = "Boeing 737",
            status = "Scheduled"
        )

        val mockPassenger = com.example.domain.model.Passenger(
            passengerId = "p1",
            uid = "u1",
            firstName = "Djerfi",
            lastName = "Fatma",
            passportNumber = "AB123456",
            nationality = "Algerian",
            dateOfBirth = "1990-01-01",
            expiryDate = null,
            seatNumber = "12A",
            checkinStatus = "PENDING"
        )

        return listOf(
            Booking(
                bookingId = "BB9XC2",
                pnr = "BB9XC2",
                lastName = "Fatma",
                status = CheckInStatus.CONFIRMED,
                flight = mockFlight,
                passengers = listOf(mockPassenger),
                gate = "G24",
                bookingRef = "BB9XC2"
            ),
            Booking(
                bookingId = "AA1BB2",
                pnr = "AA1BB2",
                lastName = "Fatma",
                status = CheckInStatus.CHECK_IN_OPEN,
                flight = mockFlight.copy(flightNumber = "SW405"),
                passengers = listOf(mockPassenger),
                gate = "H12",
                bookingRef = "AA1BB2"
            )
        )
    }

    override fun getBooking(pnr: String, lastName: String, callback: (Result<Booking>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localFullBooking = bookingDao?.getFullBookingByPnr(pnr, lastName)
                if (localFullBooking != null) {
                    callback(Result.success(localFullBooking.toDomain().booking))
                } else {
                    bookingDataSource?.getBooking(pnr, lastName) { result ->
                        result.onSuccess { bookingDto ->
                            fetchFullBookingAndCache(bookingDto, callback = callback)
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

    override fun getBookingsByUid(uid: String, callback: (Result<List<Booking>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localBookings = bookingDao?.getFullBookingsByUid(uid)
                if (localBookings != null && localBookings.isNotEmpty()) {
                    callback(Result.success(localBookings.map { it.toDomain().booking }))
                } else {
                    bookingDataSource?.getBookingsByUid(uid) { result ->
                        result.onSuccess { bookingDtos ->
                            if (bookingDtos.isEmpty()) {
                                callback(Result.success(emptyList()))
                                return@onSuccess
                            }

                            val fullBookings = mutableListOf<Booking>()
                            var processedCount = 0
                            bookingDtos.forEach { dto ->
                                fetchFullBookingAndCache(dto, uid) { fullResult ->
                                    fullResult.onSuccess { fullBookings.add(it) }
                                    processedCount++
                                    if (processedCount == bookingDtos.size) {
                                        callback(Result.success(fullBookings))
                                    }
                                }
                            }
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

    private fun fetchFullBookingAndCache(
        bookingDto: BookingDto,
        uid: String = "",
        callback: (Result<Booking>) -> Unit
    ) {
        // 1. Fetch Passengers
        bookingDataSource?.getPassengersByBookingId(bookingDto.bookingId) { passengerResult ->
            passengerResult.onSuccess { passengerDtos ->
                // 2. Fetch Flight
                flightDataSource?.getItinerary(bookingDto.pnr) { flightResult ->
                    flightResult.onSuccess { flightDto ->
                        val domainFlight = flightDto.toDomain()
                        val domainPassengers = passengerDtos.map { it.toDomain() }
                        val booking = Booking(
                            bookingId = bookingDto.bookingId,
                            pnr = bookingDto.pnr,
                            lastName = bookingDto.lastName,
                            status = try { CheckInStatus.valueOf(bookingDto.status.name) } catch (e: Exception) { CheckInStatus.CONFIRMED },
                            flight = domainFlight,
                            passengers = domainPassengers
                        )
                        
                        // Cache locally
                        CoroutineScope(Dispatchers.IO).launch {
                            flightDao?.insertFlight(domainFlight.toEntity())
                            bookingDao?.insertBooking(booking.toEntity(uid))
                            bookingDao?.insertPassengers(domainPassengers.map { it.toEntity(booking.bookingId) })
                        }

                        callback(Result.success(booking))
                    }.onFailure {
                        callback(Result.failure(it))
                    }
                }
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }
}
