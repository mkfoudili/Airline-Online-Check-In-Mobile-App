package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.example.data.local.entity.BoardingPassEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BoardingPassDao {

    @Upsert
    suspend fun insertBoardingPass(entity: BoardingPassEntity)

    @Query("SELECT * FROM boarding_passes WHERE passengerId = :passengerId")
    fun getBoardingPassByPassenger(passengerId: String): Flow<BoardingPassEntity?>

    @Query("SELECT * FROM boarding_passes WHERE flightId = :flightId")
    fun getBoardingPassByFlight(flightId: String): Flow<BoardingPassEntity?>

    @Query("SELECT * FROM boarding_passes")
    fun getAllBoardingPasses(): Flow<List<BoardingPassEntity>>

    @Query("SELECT * FROM boarding_passes WHERE passId = :passId")
    suspend fun getBoardingPassById(passId: String): BoardingPassEntity?

    @Query("DELETE FROM boarding_passes")
    suspend fun deleteAll()

    @Query("UPDATE boarding_passes SET isSyncedWithServer = 1 WHERE passId = :passId")
    suspend fun markAsSynced(passId: String)
}