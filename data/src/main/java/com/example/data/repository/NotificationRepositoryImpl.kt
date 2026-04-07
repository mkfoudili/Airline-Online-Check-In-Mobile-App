package com.example.data.repository

import com.example.data.mapper.toDomain
import com.example.data.remote.NotificationDataSource
import com.example.domain.model.Notification
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
}