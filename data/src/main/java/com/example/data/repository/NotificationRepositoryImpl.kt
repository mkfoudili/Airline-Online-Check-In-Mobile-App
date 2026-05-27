package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.retrofit.Endpoint
import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * Implementation of [NotificationRepository] using direct network calls.
 * No local persistence (Room) is used.
 */
class NotificationRepositoryImpl @Inject constructor(
    private val api: Endpoint
) : NotificationRepository {

    override suspend fun getNotifications(uid: String): Result<List<Notification>> = withContext(Dispatchers.IO) {
        try {
            val dtos = api.getNotifications()
            Result.success(dtos.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAsRead(notificationId: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.markAsRead(notificationId)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun markAllAsRead(uid: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            api.markAllAsRead()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUnreadCount(uid: String): Result<Int> = withContext(Dispatchers.IO) {
        try {
            // Calculated from the notifications list as the backend doesn't provide a specific count endpoint
            val dtos = api.getNotifications()
            val unreadCount = dtos.count { !it.isRead }
            Result.success(unreadCount)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
