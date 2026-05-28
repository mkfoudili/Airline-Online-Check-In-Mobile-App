package com.example.domain.repository

import com.example.domain.model.Notification


/**
 * Repository for managing notifications and push tokens.
 */
interface NotificationRepository {
    suspend fun getNotifications(uid: String): Result<List<Notification>>
    suspend fun markAsRead(notificationId: String): Result<Unit>
    suspend fun markAllAsRead(uid: String): Result<Unit>
    suspend fun getUnreadCount(uid: String): Result<Int>
    suspend fun registerToken(token: String): Result<Unit>
}
