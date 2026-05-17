package com.example.data.repository

import com.example.data.local.dao.BoardingPassDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BoardingPassRepositoryImpl @Inject constructor(
    private val boardingPassDao: BoardingPassDao,
    private val endpoint: Endpoint
) : BoardingPassRepository {

    override fun getBoardingPass(passengerId: String): Flow<BoardingPass?> =
        boardingPassDao.getBoardingPassByPassenger(passengerId).map { it?.toDomain() }

    override fun getBoardingPassByFlight(flightId: String): Flow<BoardingPass?> =
        boardingPassDao.getBoardingPassByFlight(flightId).map { it?.toDomain() }

    override fun getAllBoardingPasses(): Flow<List<BoardingPass>> =
        boardingPassDao.getAllBoardingPasses().map { entities -> entities.map { it.toDomain() } }

    override suspend fun saveBoardingPassLocally(boardingPass: BoardingPass) {
        boardingPassDao.insertBoardingPass(boardingPass.toEntity())
    }

    override suspend fun generateAndSyncFromServer(passengerId: String): BoardingPass {
        val response = endpoint.generateBoardingPass(mapOf("passengerId" to passengerId))
        val boardingPass = response.data.toDomain()
        // Persist locally, subsequent reads come from Room (offline-first)
        boardingPassDao.insertBoardingPass(boardingPass.toEntity())
        boardingPassDao.markAsSynced(boardingPass.passId)
        return boardingPass
    }

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
                passId = "BP-passenger-fatma-001",
                passengerId = "passenger-fatma-001",
                flightId = "flight-ah1042-001",
                flightNumber = "AH 1042",
                origin = "ALG",
                originCity = "Algiers",
                destination = "CDG",
                destinationCity = "Paris",
                passengerName = "Fatma Djerfi",
                seatNumber = "12A",
                gate = "A12",
                boardingTime = "07:50",
                departureTime = System.currentTimeMillis() + 86_400_000L,
                arrivalTime = System.currentTimeMillis() + 90_000_000L,
                bookingReference = "BB9XC2",
                terminal = "T1",
                qrCodeData = "CHECKIN_PASS:BP-passenger-fatma-001",
                issuedAt = System.currentTimeMillis(),
                isSyncedWithServer = true
            ),
            BoardingPass(
                passId = "BP-passenger-youcef-002",
                passengerId = "passenger-youcef-002",
                flightId = "flight-ah2056-002",
                flightNumber = "AH 2056",
                origin = "ORN",
                originCity = "Oran",
                destination = "LHR",
                destinationCity = "London",
                passengerName = "Youcef Benali",
                seatNumber = "7C",
                gate = "B05",
                boardingTime = "09:30",
                departureTime = System.currentTimeMillis() + 172_800_000L,
                arrivalTime = System.currentTimeMillis() + 180_000_000L,
                bookingReference = "XY5F3K",
                terminal = "T2",
                qrCodeData = "CHECKIN_PASS:BP-passenger-youcef-002",
                issuedAt = System.currentTimeMillis(),
                isSyncedWithServer = false
            )
        )
    }
}