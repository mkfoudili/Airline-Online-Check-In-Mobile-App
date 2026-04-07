package com.example.data.mapper

import com.example.data.local.entity.SeatMapEntity
import com.example.data.remote.dto.SeatMapDto
import com.example.domain.model.Seat

fun SeatMapDto.toDomain(): Seat {
    return Seat(
        seatId = this.seatId,
        flightId = this.flightId,
        seatNumber = this.seatNumber,
        seatClass = this.seatClass,
        isAvailable = this.isAvailable,
        isPremium = this.isPremium,
        occupiedBy = this.occupiedBy
    )
}

fun SeatMapEntity.toDomain(): Seat {
    return Seat(
        seatId = this.seatId,
        flightId = this.flightId,
        seatNumber = this.seatNumber,
        seatClass = this.seatClass,
        isAvailable = this.isAvailable,
        isPremium = this.isPremium,
        occupiedBy = this.occupiedBy
    )
}

fun Seat.toEntity(): SeatMapEntity {
    return SeatMapEntity(
        seatId = this.seatId,
        flightId = this.flightId,
        seatNumber = this.seatNumber,
        seatClass = this.seatClass,
        isAvailable = this.isAvailable,
        isPremium = this.isPremium,
        occupiedBy = this.occupiedBy
    )
}
