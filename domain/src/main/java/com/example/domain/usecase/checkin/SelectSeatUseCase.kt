package com.example.domain.usecase.checkin

import com.example.domain.model.Seat
import com.example.domain.repository.SeatRepository
import javax.inject.Inject

class SelectSeatUseCase @Inject constructor(
    private val repository: SeatRepository
) {
    suspend operator fun invoke(passengerId: String, seatNumber: String): Result<Seat> {
        return try {
            val seat = repository.selectSeat(passengerId, seatNumber)
            Result.success(seat)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
