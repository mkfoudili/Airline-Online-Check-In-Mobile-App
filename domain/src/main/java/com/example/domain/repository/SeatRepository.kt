package com.example.domain.repository

import com.example.domain.model.Seat

interface SeatRepository {
    fun getSeatMap(flightId: String, callback: (Result<List<Seat>>) -> Unit)
    fun reserveSeat(seatId: String, passengerId: String, callback: (Result<Seat>) -> Unit)
    fun releaseSeat(seatId: String, callback: (Result<Unit>) -> Unit)
}