package com.example.data.remote

import com.example.data.remote.dto.PassengerVerifyDto
import com.example.data.remote.retrofit.Endpoint
import javax.inject.Inject

/**
 * Calls the backend to verify a scanned passport against the DB.
 * Throws [PassportVerificationException] if the passport is not found.
 */
class PassengerVerifyDataSource @Inject constructor(
    private val endpoint: Endpoint
) {
    /**
     * Returns the verified [PassengerVerifyDto] from the server.
     * @throws PassportVerificationException when server returns 404 (no match)
     * @throws retrofit2.HttpException for other HTTP errors
     */
    suspend fun verifyPassport(
        passportNumber: String,
        lastName: String,
        firstName: String? = null,
        nationality: String? = null,
        dateOfBirth: String? = null,
        expiryDate: String? = null
    ): PassengerVerifyDto {
        return try {
            endpoint.verifyPassport(
                passportNumber, 
                lastName, 
                firstName, 
                nationality,
                dateOfBirth,
                expiryDate
            ).passenger
        } catch (e: retrofit2.HttpException) {
            if (e.code() == 404) {
                throw PassportVerificationException(
                    "We couldn't match this passport to any passenger on this booking. Please ensure you are scanning the correct passport in good lighting with no glare, and try again."
                )
            } else throw e
        }
    }
}

class PassportVerificationException(message: String) : Exception(message)
