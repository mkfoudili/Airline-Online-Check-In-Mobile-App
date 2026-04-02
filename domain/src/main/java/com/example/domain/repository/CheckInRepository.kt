package com.example.domain.repository

import com.example.domain.model.CheckInSession

interface CheckInRepository {
    fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit)
    fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
    fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
}