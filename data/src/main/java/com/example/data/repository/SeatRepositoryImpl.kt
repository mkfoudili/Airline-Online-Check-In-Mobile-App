package com.example.data.repository

import com.example.data.local.dao.SeatMapDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.SeatDataSource
import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SeatRepositoryImpl(
    private val seatDataSource: SeatDataSource,
    private val seatMapDao: SeatMapDao
) : SeatRepository {

    override fun getSeatMap(flightId: String, callback: (Result<List<Seat>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localSeats = seatMapDao.getSeatsByFlightId(flightId)
                if (localSeats.isNotEmpty()) {
                    callback(Result.success(localSeats.map { it.toDomain() }))
                } else {
                    seatDataSource.getSeatMap(flightId) { result ->
                        result.onSuccess { dtos ->
                            val domainSeats = dtos.map { it.toDomain() }
                            // Cache locally
                            CoroutineScope(Dispatchers.IO).launch {
                                seatMapDao.insertSeats(domainSeats.map { it.toEntity() })
                            }
                            callback(Result.success(domainSeats))
                        }.onFailure {
                            callback(Result.failure(it))
                        }
                    }
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
        }
    }

    override fun reserveSeat(seatId: String, passengerId: String, callback: (Result<Seat>) -> Unit) {
        seatDataSource.reserveSeat(seatId, passengerId) { result ->
            result.onSuccess { dto ->
                val domainSeat = dto.toDomain()
                CoroutineScope(Dispatchers.IO).launch {
                    seatMapDao.updateSeatReservation(seatId, false, passengerId)
                }
                callback(Result.success(domainSeat))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun releaseSeat(seatId: String, callback: (Result<Unit>) -> Unit) {
        seatDataSource.releaseSeat(seatId) { result ->
            result.onSuccess {
                CoroutineScope(Dispatchers.IO).launch {
                    seatMapDao.updateSeatReservation(seatId, true, null)
                }
                callback(Result.success(Unit))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }
}
