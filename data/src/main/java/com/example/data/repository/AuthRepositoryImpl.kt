package com.example.data.repository

import com.example.data.remote.AuthDataSource
import com.example.data.remote.UserDto
import com.example.domain.model.User
import com.example.domain.repository.AuthRepository
import com.example.domain.validation.RegistrationRequest

class AuthRepositoryImpl(private val authDataSource: AuthDataSource) : AuthRepository {

    override fun register(request: RegistrationRequest, callback: (Result<User>) -> Unit) {
        authDataSource.register(request) { userDto ->
            if (userDto != null) {
                callback(Result.success(userDto.toDomain()))
            } else {
                callback(Result.failure(Exception("Registration failed")))
            }
        }
    }

    override fun login(email: String, password: String, callback: (Result<User>) -> Unit) {
        authDataSource.login(email, password) { userDto ->
            if (userDto != null) {
                callback(Result.success(userDto.toDomain()))
            } else {
                callback(Result.failure(Exception("Login failed. Please check your credentials.")))
            }
        }
    }

    override fun emailExists(email: String, callback: (Result<Boolean>) -> Unit) {
        authDataSource.emailExists(email) { exists ->
            callback(Result.success(exists))
        }
    }

    private fun UserDto.toDomain(): User {
        return User(
            uid = this.uid,
            email = this.email ?: "",
            displayName = this.displayName,
            phoneNumber = this.phoneNumber,
            provider = this.provider
        )
    }
}
