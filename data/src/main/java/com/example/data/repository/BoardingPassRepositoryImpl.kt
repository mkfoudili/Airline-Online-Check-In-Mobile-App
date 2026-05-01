package com.example.data.repository

import com.example.data.local.dao.BoardingPassDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

class BoardingPassRepositoryImpl(
    private val boardingPassDao: BoardingPassDao
) : BoardingPassRepository {

    override fun getBoardingPass(passengerId: String): Flow<BoardingPass?> {
        return boardingPassDao
            .getBoardingPassByPassenger(passengerId)
            .map { entity -> entity?.toDomain() }
    }

    override fun getBoardingPassByFlight(flightId: String): Flow<BoardingPass?> {
        return boardingPassDao
            .getBoardingPassByFlight(flightId)
            .map { entity -> entity?.toDomain() }
    }

    override fun getAllBoardingPasses(): Flow<List<BoardingPass>> {
        return boardingPassDao
            .getAllBoardingPasses()
            .map { entities -> entities.map { it.toDomain() } }
    }

    override suspend fun saveBoardingPassLocally(boardingPass: BoardingPass) {
        boardingPassDao.insertBoardingPass(boardingPass.toEntity())
    }

    /**
     * Inserts mock boarding passes so the app always has data even when offline.
     * Only seeds if the table is empty.
     */
    override suspend fun seedMockDataIfEmpty() {
        val existing = boardingPassDao.getAllBoardingPasses().firstOrNull()
        if (existing.isNullOrEmpty()) {
            MOCK_BOARDING_PASSES.forEach { pass ->
                boardingPassDao.insertBoardingPass(pass.toEntity())
            }
        }
    }

    companion object {
        val MOCK_BOARDING_PASSES = listOf(
            BoardingPass(
                passId = "BP-001",
                passengerId = "p1",
                flightId = "f1",
                flightNumber = "AH 1042",
                origin = "ALG",
                originCity = "Algiers",
                destination = "CDG",
                destinationCity = "Paris",
                passengerName = "Djerfi Fatma",
                seatNumber = "12A",
                gate = "A12",
                boardingTime = "07:50",
                departureTime = System.currentTimeMillis() + 86_400_000L,
                arrivalTime = System.currentTimeMillis() + 90_000_000L,
                bookingReference = "BB9XC2",
                terminal = "T1",
                qrCodeData = "BOARDING:AH1042:BB9XC2:12A:ALG-CDG",
                issuedAt = System.currentTimeMillis(),
                isSyncedWithServer = true
            ),
            BoardingPass(
                passId = "BP-002",
                passengerId = "p2",
                flightId = "f2",
                flightNumber = "AH 2056",
                origin = "ORN",
                originCity = "Oran",
                destination = "LHR",
                destinationCity = "London",
                passengerName = "Benali Youcef",
                seatNumber = "7C",
                gate = "B05",
                boardingTime = "10:15",
                departureTime = System.currentTimeMillis() + 172_800_000L,
                arrivalTime = System.currentTimeMillis() + 180_000_000L,
                bookingReference = "XY5F3K",
                terminal = "T2",
                qrCodeData = "BOARDING:AH2056:XY5F3K:7C:ORN-LHR",
                issuedAt = System.currentTimeMillis(),
                isSyncedWithServer = false
            )
        )
    }
}