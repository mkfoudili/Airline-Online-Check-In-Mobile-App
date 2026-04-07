package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.mapper.toDto
import com.example.data.remote.CheckInDataSource
import com.example.domain.model.CheckInSession
import com.example.domain.repository.CheckInRepository

class CheckInRepositoryImpl(private val checkInDataSource: CheckInDataSource) : CheckInRepository {

    override fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit) {
        checkInDataSource.getSession(sessionId) { result ->
            result.onSuccess { sessionDto ->
                callback(Result.success(sessionDto.toDomain()))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        checkInDataSource.updateSession(dto) { result ->
            result.onSuccess {
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }

    override fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit) {
        val dto = session.toDto()
        checkInDataSource.createSession(dto) { result ->
            result.onSuccess {
                callback(Result.success(session))
            }.onFailure {
                callback(Result.failure(it))
            }
        }
    }
}
