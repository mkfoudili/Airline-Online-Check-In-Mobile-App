package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.dto.SelectSeatRequest
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository
import javax.inject.Inject

class SeatRepositoryImpl @Inject constructor(
    private val api: Endpoint
) : SeatRepository {

    override suspend fun getSeatMap(flightId: String): List<Seat> {
        return try {
            api.getSeatMap(flightId).map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun selectSeat(passengerId: String, seatNumber: String, uid: String?): Seat {
        val request = SelectSeatRequest(seatNumber = seatNumber, uid = uid)
        val dto = api.selectSeat(passengerId, request)
        return dto.toDomain()
    }
}
