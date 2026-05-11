package com.example.domain.repository

import com.example.domain.model.BaggageDeclaration
import com.example.domain.model.CheckInSession
import com.example.domain.model.Passenger

interface CheckInRepository {
    fun getSession(sessionId: String, callback: (Result<CheckInSession>) -> Unit)
    fun updateSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
    fun createSession(session: CheckInSession, callback: (Result<CheckInSession>) -> Unit)
    fun getPassengerForReview(): Passenger
    fun getBaggageDeclaration(): BaggageDeclaration
    fun declareBaggage(declaration: BaggageDeclaration)
}