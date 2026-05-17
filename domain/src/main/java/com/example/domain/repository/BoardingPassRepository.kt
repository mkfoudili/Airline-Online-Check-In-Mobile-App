package com.example.domain.repository

import com.example.domain.model.BoardingPass
import kotlinx.coroutines.flow.Flow

interface BoardingPassRepository {

    /** Returns the boarding pass for a passenger from the local cache. */
    fun getBoardingPass(passengerId: String): Flow<BoardingPass?>

    /** Returns the boarding pass for a flight from the local cache. */
    fun getBoardingPassByFlight(flightId: String): Flow<BoardingPass?>

    /** Returns all locally cached boarding passes. */
    fun getAllBoardingPasses(): Flow<List<BoardingPass>>

    /** Saves (or updates) a boarding pass in the local cache. */
    suspend fun saveBoardingPassLocally(boardingPass: BoardingPass)

    suspend fun generateAndSyncFromServer(passengerId: String): BoardingPass

    /** Seeds mock data so the app has boarding passes available offline on first launch. */
    suspend fun seedMockDataIfEmpty()
}