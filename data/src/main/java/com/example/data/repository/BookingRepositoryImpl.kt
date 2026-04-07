package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.BookingDataSource
import com.example.data.remote.dto.BookingDto
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository

class BookingRepositoryImpl(
    private val bookingDataSource: BookingDataSource,
    private val flightDataSource: FlightDataSource
) : BookingRepository {

    override fun getBooking(pnr: String, lastName: String, callback: (Result<Booking>) -> Unit) {
        bookingDataSource.getBooking(pnr, lastName) { result ->
            result.onSuccess { bookingDto ->
                fetchFullBooking(bookingDto, callback)
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun getBookingsByUid(uid: String, callback: (Result<List<Booking>>) -> Unit) {
        bookingDataSource.getBookingsByUid(uid) { result ->
            result.onSuccess { bookingDtos ->
                val fullBookings = mutableListOf<Booking>()
                var processedCount = 0
                
                if (bookingDtos.isEmpty()) {
                    callback(Result.success(emptyList()))
                    return@onSuccess
                }

                bookingDtos.forEach { dto ->
                    fetchFullBooking(dto) { fullResult ->
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

    private fun fetchFullBooking(bookingDto: BookingDto, callback: (Result<Booking>) -> Unit) {
        // 1. Fetch Passengers
        bookingDataSource.getPassengersByBookingId(bookingDto.bookingId) { passengerResult ->
            passengerResult.onSuccess { passengerDtos ->
                // 2. Fetch Flight (Assuming flightId is available in booking or derived)
                // For this implementation, we'll search for the flight associated with the booking's PNR
                flightDataSource.getItinerary(bookingDto.pnr) { flightResult ->
                    flightResult.onSuccess { flightDto ->
                        val booking = Booking(
                            bookingId = bookingDto.bookingId,
                            pnr = bookingDto.pnr,
                            lastName = bookingDto.lastName,
                            status = bookingDto.status,
                            flight = flightDto.toDomain(),
                            passengers = passengerDtos.map { it.toDomain() }
                        )
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
