package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.dto.RegisterTokenRequest
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [NotificationRepository] using direct network calls.
 */
class NotificationRepositoryImpl @Inject constructor(
    private val api: Endpoint
) : NotificationRepository {

    override suspend fun getNotifications(uid: String): Result<List<Notification>> = withContext(Dispatchers.IO) {
        try {
            val response = api.getNotifications()
            if (response.success) {
                Result.success(response.data.map { it.toDomain() })
            } else {
                Result.failure(Exception(response.message ?: "Failed to retrieve notifications"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.markAsRead(notificationId)
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to mark notification as read"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllAsRead(uid: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.markAllAsRead()
            if (response.success) {
                Result.success(Unit)
            } else {
                Result.failure(Exception(response.message ?: "Failed to mark all as read"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadCount(uid: String): Result<Int> = withContext(Dispatchers.IO) {
        try {
            val response = api.getNotifications()
            if (response.success) {
                val unreadCount = response.data.count { !it.isRead }
                Result.success(unreadCount)
            } else {
                Result.failure(Exception(response.message ?: "Failed to fetch unread count"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerToken(token: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val response = api.registerToken(RegisterTokenRequest(token))
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Failed to register token: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
