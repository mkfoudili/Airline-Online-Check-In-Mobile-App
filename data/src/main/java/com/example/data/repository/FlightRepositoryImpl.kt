package com.example.data.repository

import com.example.data.local.dao.FlightDao
import com.example.data.local.entity.FlightEntity
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.BookingDataSource
import com.example.data.remote.FlightDataSource
import com.example.domain.model.Flight
import com.example.domain.repository.FlightRepository
import javax.inject.Inject

class FlightRepositoryImpl @Inject constructor(
    private val flightDataSource: FlightDataSource,
    private val flightDao: FlightDao,
    private val bookingDataSource: BookingDataSource
) : FlightRepository {

    override suspend fun getFlightById(flightId: String): Result<Flight> {
        return try {
            val dto = flightDataSource.getFlightById(flightId)
            val flight = dto.toDomain()
            flightDao.insertFlight(flight.toEntity())
            Result.success(flight)
        } catch (e: Exception) {
            val cached = flightDao.getFlightById(flightId)
            if (cached != null) Result.success(cached.toDomain())
            else Result.failure(e)
        }
    }

    override suspend fun getAllCachedFlights(): List<Flight> {
        return flightDao.getAllFlights().map { it.toDomain() }
    }

    override suspend fun refreshFlightsFromRemote(uid: String): Result<Unit> {
        return try {

            val bookingDtos = bookingDataSource.getBookingsByUid(uid)
            val flightsToCache = mutableListOf<FlightEntity>()
            bookingDtos.forEach { bookingDto ->

                val flightEntity = try {
                    val freshFlight =
                        flightDataSource.getFlightById(
                            bookingDto.flight.flightId
                        )
                    freshFlight.toDomain().toEntity()

                } catch (e: Exception) {
                    // fallback sur les données du booking
                    bookingDto.flight.toDomain().toEntity()
                }

                flightsToCache.add(flightEntity)
            }

            // remplacer le cache uniquement quand tout est prêt
            flightDao.deleteAllFlights()
            flightDao.insertFlights(flightsToCache)

            Result.success(Unit)

        } catch (e: Exception) {
            // on garde l'ancien cache intact
            Result.failure(e)
        }
    }
}