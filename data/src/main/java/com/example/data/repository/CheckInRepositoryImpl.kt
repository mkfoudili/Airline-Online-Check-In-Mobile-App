package com.example.data.repository

import com.example.data.local.dao.CheckInSessionDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toDto
import com.example.data.mapper.toEntity
import com.example.data.remote.CheckInDataSource
import com.example.domain.model.CheckInSession
import com.example.domain.repository.CheckInRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CheckInRepositoryImpl(
    private val checkInDataSource: CheckInDataSource,
    private val checkInSessionDao: CheckInSessionDao
) : CheckInRepository {

    override fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localSession = checkInSessionDao.getSession(sessionId)
                if (localSession != null) {
                    callback(Result.success(localSession.toDomain()))
                } else {
                    checkInDataSource.getSession(sessionId) { result ->
                        result.onSuccess { sessionDto ->
                            val session = sessionDto.toDomain()
                            // Cache locally
                            CoroutineScope(Dispatchers.IO).launch {
                                checkInSessionDao.insertSession(session.toEntity())
                            }
                            callback(Result.success(session))
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

    override fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        val entity = session.toEntity()
        
        checkInDataSource.updateSession(dto) { result ->
            result.onSuccess {
                CoroutineScope(Dispatchers.IO).launch {
                    checkInSessionDao.updateSession(entity)
                }
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        val entity = session.toEntity()

        checkInDataSource.createSession(dto) { result ->
            result.onSuccess {
                CoroutineScope(Dispatchers.IO).launch {
                    checkInSessionDao.insertSession(entity)
                }
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }
}
