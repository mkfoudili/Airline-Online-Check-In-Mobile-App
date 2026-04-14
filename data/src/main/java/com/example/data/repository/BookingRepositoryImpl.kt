package com.example.data.repository

import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.FlightDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.BookingDataSource
import com.example.data.remote.dto.BookingDto
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BookingRepositoryImpl(
    private val bookingDataSource: BookingDataSource,
    private val flightDataSource: FlightDataSource,
    private val bookingDao: BookingDao,
    private val flightDao: FlightDao
) : BookingRepository {

    override fun getBooking(pnr: String, lastName: String, callback: (Result<Booking>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localFullBooking = bookingDao.getFullBookingByPnr(pnr, lastName)
                if (localFullBooking != null) {
                    callback(Result.success(localFullBooking.toDomain().booking))
                } else {
                    bookingDataSource.getBooking(pnr, lastName) { result ->
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
                val localBookings = bookingDao.getFullBookingsByUid(uid)
                if (localBookings.isNotEmpty()) {
                    callback(Result.success(localBookings.map { it.toDomain().booking }))
                } else {
                    bookingDataSource.getBookingsByUid(uid) { result ->
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
        bookingDataSource.getPassengersByBookingId(bookingDto.bookingId) { passengerResult ->
            passengerResult.onSuccess { passengerDtos ->
                // 2. Fetch Flight
                flightDataSource.getItinerary(bookingDto.pnr) { flightResult ->
                    flightResult.onSuccess { flightDto ->
                        val domainFlight = flightDto.toDomain()
                        val domainPassengers = passengerDtos.map { it.toDomain() }
                        val booking = Booking(
                            bookingId = bookingDto.bookingId,
                            pnr = bookingDto.pnr,
                            lastName = bookingDto.lastName,
                            status = bookingDto.status,
                            flight = domainFlight,
                            passengers = domainPassengers
                        )
                        
                        // Cache locally
                        CoroutineScope(Dispatchers.IO).launch {
                            flightDao.insertFlight(domainFlight.toEntity())
                            bookingDao.insertBooking(booking.toEntity(uid))
                            bookingDao.insertPassengers(domainPassengers.map { it.toEntity(booking.bookingId) })
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
