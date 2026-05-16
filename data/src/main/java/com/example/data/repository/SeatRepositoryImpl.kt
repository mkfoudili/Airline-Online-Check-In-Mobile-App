package com.example.data.repository

import com.example.data.local.dao.SeatMapDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.dto.SelectSeatRequest
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SeatRepositoryImpl @Inject constructor(
    private val api: Endpoint,
    private val seatMapDao: SeatMapDao
) : SeatRepository {

    override suspend fun getSeatMap(flightId: String): List<Seat> = withContext(Dispatchers.IO) {
        try {
            // Try fetching from remote first to get up-to-date availability
            val remoteSeats = api.getSeatMap(flightId)
            val domainSeats = remoteSeats.map { it.toDomain() }
            
            // Cache in local DB
            seatMapDao.insertSeats(domainSeats.map { it.toEntity() })
            
            domainSeats
        } catch (e: Exception) {
            // Fallback to local cache if offline
            val localSeats = seatMapDao.getSeatsByFlightId(flightId)
            if (localSeats.isNotEmpty()) {
                localSeats.map { it.toDomain() }
            } else {
                throw e
            }
        }
    }

    override suspend fun selectSeat(passengerId: String, seatNumber: String): Seat = withContext(Dispatchers.IO) {
        val response = api.selectSeat(passengerId, SelectSeatRequest(seatNumber))
        val domainSeat = response.toDomain()
        
        // Update local cache
        seatMapDao.updateSeatReservation(domainSeat.seatId, false, passengerId)
        
        domainSeat
    }
}
