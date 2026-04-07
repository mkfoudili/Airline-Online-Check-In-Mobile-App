package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.SeatDataSource
import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository

class SeatRepositoryImpl(private val seatDataSource: SeatDataSource) : SeatRepository {

    override fun getSeatMap(flightId: String, callback: (Result<List<Seat>>) -> Unit) {
        seatDataSource.getSeatMap(flightId) { result ->
            result.onSuccess { dtos ->
                callback(Result.success(dtos.map { it.toDomain() }))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun reserveSeat(seatId: String, passengerId: String, callback: (Result<Seat>) -> Unit) {
        seatDataSource.reserveSeat(seatId, passengerId) { result ->
            result.onSuccess { dto ->
                callback(Result.success(dto.toDomain()))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun releaseSeat(seatId: String, callback: (Result<Unit>) -> Unit) {
        seatDataSource.releaseSeat(seatId) { result ->
            callback(result)
        }
    }
}
