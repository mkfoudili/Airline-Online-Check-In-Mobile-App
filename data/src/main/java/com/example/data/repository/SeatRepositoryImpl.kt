package com.example.data.repository

import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository
import javax.inject.Inject

class SeatRepositoryImpl @Inject constructor(
    private val api: Endpoint
) : SeatRepository {

    override suspend fun getSeatMap(flightId: String): List<Seat> {
        // TODO
        return emptyList()
    }

    override suspend fun selectSeat(passengerId: String, seatNumber: String): Seat {
        // TODO
        return Seat(
            seatId = seatNumber,
            flightId = "",
            seatNumber = seatNumber,
            seatClass = "ECONOMY",
            isAvailable = false,
            isPremium = false,
            occupiedBy = passengerId
        )
    }
}