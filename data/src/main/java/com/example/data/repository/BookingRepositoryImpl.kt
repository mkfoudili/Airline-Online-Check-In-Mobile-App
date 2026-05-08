package com.example.data.repository

import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.FlightDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import com.example.data.remote.dto.BookingDto
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.repository.BookingRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingDataSource: BookingDataSource,
    private val flightDataSource: FlightDataSource?,
    private val bookingDao: BookingDao?,
    private val flightDao: FlightDao?
) : BookingRepository {

    override suspend fun getUpcomingBookings(uid: String): Result<List<Booking>> {
        return try {
            val bookingDtos = bookingDataSource.getUpcomingBookingsByUid(uid)
            val bookings = bookingDtos.map { it.toDomain() }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBooking(pnr: String, lastName: String): Result<Booking> {
        return try {
            val dto = bookingDataSource.getBooking(pnr, lastName)
            if (dto != null) {
                Result.success(dto.toDomain())
            } else {
                Result.failure(Exception("Booking not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBookingsByUid(uid: String): Result<List<Booking>> {
        return try {
            val bookingDtos = bookingDataSource.getBookingsByUid(uid)
            val bookings = bookingDtos.map { it.toDomain() }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
