package com.example.data.repository

import com.example.data.local.dao.BookingDao
import com.example.data.local.dao.FlightDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Booking
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

    // ─── Upcoming bookings : réseau d'abord, Room en fallback ─────────────────

    override suspend fun getUpcomingBookings(uid: String): Result<List<Booking>> {
        return try {
            val dtos = bookingDataSource.getUpcomingBookingsByUid(uid)
            val bookings = dtos.map { it.toDomain() }
            persistBookingsToRoom(uid, bookings)
            Result.success(bookings)
        } catch (e: Exception) {
            // BUGFIX: FlightItineraryEntity.toDomain() retourne FlightItinerary, pas Booking.
            // On utilise booking.toDomain(flight, passengers) pour obtenir List<Booking>.
            val cached: List<Booking> = bookingDao
                ?.getUpcomingFullBookingsByUid(uid)
                ?.map { entity ->
                    entity.booking.toDomain(
                        flight = entity.flight.toDomain(),
                        passengers = entity.passengers.map { it.toDomain() }
                    )
                }
                ?: emptyList()
            if (cached.isNotEmpty()) Result.success(cached)
            else Result.failure(e)
        }
    }

    // ─── Search par PNR + lastName ────────────────────────────────────────────

    override suspend fun getBooking(pnr: String, lastName: String): Result<Booking> {
        return try {
            val dto = bookingDataSource.getBooking(pnr, lastName)
            if (dto != null) {
                val booking = dto.toDomain()
                persistBookingsToRoom("", listOf(booking))
                Result.success(booking)
            } else {
                Result.failure(Exception("booking_not_found"))
            }
        } catch (e: retrofit2.HttpException) {
            when (e.code()) {
                404 -> Result.failure(Exception("booking_not_found"))
                else -> Result.failure(e)
            }
        } catch (e: Exception) {
            // BUGFIX: même correction — FlightItineraryEntity → Booking via booking.toDomain()
            val cached: Booking? = bookingDao
                ?.getFullBookingByPnr(pnr.uppercase().trim(), lastName.trim())
                ?.let { entity ->
                    entity.booking.toDomain(
                        flight = entity.flight.toDomain(),
                        passengers = entity.passengers.map { it.toDomain() }
                    )
                }
            if (cached != null) Result.success(cached)
            else Result.failure(Exception("booking_not_found"))
        }
    }

    // ─── All bookings ─────────────────────────────────────────────────────────

    override suspend fun getBookingsByUid(uid: String): Result<List<Booking>> {
        return try {
            val dtos = bookingDataSource.getBookingsByUid(uid)
            val bookings = dtos.map { it.toDomain() }
            persistBookingsToRoom(uid, bookings)
            Result.success(bookings)
        } catch (e: Exception) {
            // BUGFIX: même correction — FlightItineraryEntity → Booking via booking.toDomain()
            val cached: List<Booking> = bookingDao
                ?.getFullBookingsByUid(uid)
                ?.map { entity ->
                    entity.booking.toDomain(
                        flight = entity.flight.toDomain(),
                        passengers = entity.passengers.map { it.toDomain() }
                    )
                }
                ?: emptyList()
            if (cached.isNotEmpty()) Result.success(cached)
            else Result.failure(e)
        }
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    private fun persistBookingsToRoom(uid: String, bookings: List<Booking>) {
        val dao = bookingDao ?: return
        val fDao = flightDao ?: return
        CoroutineScope(Dispatchers.IO).launch {
            bookings.forEach { booking ->
                fDao.insertFlight(booking.flight.toEntity())
                dao.insertBooking(booking.toEntity(uid))
                dao.insertPassengers(booking.passengers.map { it.toEntity(booking.bookingId) })
            }
        }
    }
}