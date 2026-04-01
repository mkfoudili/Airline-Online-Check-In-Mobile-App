package com.example.data.repository

import androidx.compose.ui.input.key.type
import com.example.data.remote.NotificationDataSource
import com.example.data.remote.NotificationDto
import com.example.domain.model.Notification
import com.example.domain.model.NotificationType
import com.example.domain.repository.NotificationRepository

class NotificationRepositoryImpl(val notificationDataSource: NotificationDataSource): NotificationRepository {
    override fun getNotifications(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        notificationDataSource.getNotifications(uid) { result ->
            result.onSuccess { dtos ->
                callback(Result.success(dtos.map { it.toDomain() }))
            }.onFailure { callback(Result.failure(it)) }
        }
    }

    override fun getUnreadCount(uid: String, callback: (Result<Int>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun markAllAsRead(uid: String, callback: (Result<Unit>) -> Unit) {
        TODO("Not yet implemented")
    }

    override fun markAsRead(notificationId: String, callback: (Result<Unit>) -> Unit) {
        TODO("Not yet implemented")
    }

    private fun NotificationDto.toDomain(): Notification {
        return Notification(
            notificationId = this.notificationId,
            passengerId = this.passengerId ?: "",
            title = this.title ?: "",
            body = this.body ?: "",
            type = try { NotificationType.valueOf(this.type ?: "DELAY") }
            catch (e: Exception) { NotificationType.DELAY },
            isRead = this.isRead,
            createdAt = this.createdAt?.time ?: 0L
        )
    }
}