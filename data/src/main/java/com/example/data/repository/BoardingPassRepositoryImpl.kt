package com.example.data.repository

import com.example.data.local.dao.BoardingPassDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.BoardingPass
import com.example.domain.repository.BoardingPassRepository
import kotlinx.coroutines.flow.Flow
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

    override suspend fun getBoardingPassesByUid(uid: String): List<BoardingPass> =
        boardingPassDao.getBoardingPassesByUid(uid).map { it.toDomain() }

    override suspend fun saveBoardingPassLocally(boardingPass: BoardingPass) {
        boardingPassDao.insertBoardingPass(boardingPass.toEntity())
    }

    override suspend fun generateAndSyncFromServer(passengerId: String): BoardingPass {
        val response = endpoint.generateBoardingPass(mapOf("passengerId" to passengerId))
        val boardingPass = response.data.toDomain()
        boardingPassDao.insertBoardingPass(boardingPass.toEntity())
        boardingPassDao.markAsSynced(boardingPass.passId)
        return boardingPass
    }

    override suspend fun refreshBoardingPassesFromRemote(uid: String): Result<Unit> {
        return try {
            val response = endpoint.getMyBoardingPasses()
            response.data.forEach { dto ->
                val boardingPass = dto.toDomain(contextUid = uid)
                boardingPassDao.insertBoardingPass(boardingPass.toEntity())
                boardingPassDao.markAsSynced(boardingPass.passId)
            }
            Result.success(Unit)
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) Result.success(Unit)
            else Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}