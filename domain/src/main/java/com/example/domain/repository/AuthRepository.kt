package com.example.domain.repository

import com.example.domain.model.User
import com.example.domain.validation.RegistrationRequest

/**
 * Interface for Authentication Repository in the Domain layer.
 */
interface AuthRepository {
    fun register(request: RegistrationRequest, callback: (Result<User>) -> Unit)
    fun login(email: String, password: String, callback: (Result<User>) -> Unit)
    fun emailExists(email: String, callback: (Result<Boolean>) -> Unit)
}