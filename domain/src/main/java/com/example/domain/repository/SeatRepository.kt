package com.example.domain.repository

import com.example.domain.model.Seat

interface SeatRepository {
    suspend fun getSeatMap(flightId: String): List<Seat>
    suspend fun selectSeat(passengerId: String, seatNumber: String): Seat
}
