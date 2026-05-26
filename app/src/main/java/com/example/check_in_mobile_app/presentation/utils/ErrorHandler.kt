package com.example.check_in_mobile_app.presentation.utils

import retrofit2.HttpException
import java.io.IOException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

fun Throwable.toUserFriendlyMessage(): String {
    val msg = this.message ?: ""
    
    // Catch common connection error strings that might be in generic IOExceptions
    if (msg.contains("failed to connect", ignoreCase = true) || 
        msg.contains("unable to resolve host", ignoreCase = true) ||
        msg.contains("connection refused", ignoreCase = true)) {
        return "Cannot reach the server. Please check your internet connection."
    }

    return when (this) {
        is UnknownHostException, is ConnectException -> {
            "No internet connection or server is unreachable. Please try again."
        }
        is SocketTimeoutException -> {
            "The connection timed out. The server is taking too long to respond."
        }
        is HttpException -> {
            when (this.code()) {
                400 -> "Invalid information provided. Please check your entries."
                401 -> "Login failed. Please check your email and password."
                403 -> "You don't have permission to perform this action."
                404 -> "The requested information could not be found on the server."
                409 -> "This account already exists. Please try logging in instead."
                429 -> "Too many requests. Please slow down and try again in a moment."
                500 -> "Server is having trouble. Our team is looking into it."
                503 -> "Server is temporarily down for maintenance. Please try again later."
                else -> "Server error (${this.code()}). Please try again later."
            }
        }
        is IOException -> {
            "A network error occurred. Please check your connection."
        }
        else -> {
            if (msg.contains("timeout", ignoreCase = true)) {
                "Request timed out. Please try again."
            } else {
                "An unexpected error occurred. Please try again later."
            }
        }
    }
}
