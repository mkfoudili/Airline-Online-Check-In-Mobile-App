package com.example.domain.repository

import com.example.domain.model.BoardingPass
import kotlinx.coroutines.flow.Flow

interface BoardingPassRepository {
    /**
     * Returns the boarding pass for a passenger as a Flow.
     * Data comes from SQLite cache first. If not found, returns mock/server data.
     */
    fun getBoardingPass(passengerId: String): Flow<BoardingPass?>

    /**
     * Returns the boarding pass for a flight as a Flow.
     */
    fun getBoardingPassByFlight(flightId: String): Flow<BoardingPass?>

    /**
     * Returns all locally cached boarding passes.
     */
    fun getAllBoardingPasses(): Flow<List<BoardingPass>>

    /**
     * Saves (or updates) a boarding pass locally.
     */
    suspend fun saveBoardingPassLocally(boardingPass: BoardingPass)

    /**
     * Seeds mock boarding passes into the local database.
     * Call on first launch so there's always data offline.
     */
    suspend fun seedMockDataIfEmpty()
}