package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.BookingDataSource
import com.example.domain.model.Booking
import com.example.domain.repository.BookingRepository
import javax.inject.Inject

class BookingRepositoryImpl @Inject constructor(
    private val bookingDataSource: BookingDataSource
) : BookingRepository {

    override suspend fun getUpcomingBookings(uid: String): Result<List<Booking>> {
        return try {
            val dtos = bookingDataSource.getUpcomingBookingsByUid(uid)
            val bookings: List<Booking> = dtos.map { dto -> dto.toDomain() }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBooking(pnr: String, lastName: String): Result<Booking> {
        return try {
            val dto = bookingDataSource.getBooking(pnr, lastName)
            if (dto != null) Result.success(dto.toDomain())
            else Result.failure(Exception("booking_not_found"))
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                404  -> Result.failure(Exception("booking_not_found"))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getBookingsByUid(uid: String): Result<List<Booking>> {
        return try {
            val dtos = bookingDataSource.getBookingsByUid(uid)
            val bookings: List<Booking> = dtos.map { dto -> dto.toDomain() }
            Result.success(bookings)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}