package com.example.data.repository

import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository

class SeatRepositoryImpl(
) : SeatRepository {

    override fun getSeatMap(flightId: String, callback: (Result<List<Seat>>) -> Unit) {
      // TODO
    }

    override fun reserveSeat(seatId: String, passengerId: String, callback: (Result<Seat>) -> Unit) {
        // TODO
    }

    override fun releaseSeat(seatId: String, callback: (Result<Unit>) -> Unit) {
        // TODO
    }
}
