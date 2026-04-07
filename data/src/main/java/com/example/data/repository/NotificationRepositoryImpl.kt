package com.example.data.repository

import com.example.data.local.dao.NotificationDao
import com.example.data.mapper.toDomain
import com.example.data.mapper.toEntity
import com.example.data.remote.NotificationDataSource
import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class NotificationRepositoryImpl(
    private val notificationDataSource: NotificationDataSource,
    private val notificationDao: NotificationDao
) : NotificationRepository {

    override fun getNotifications(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val localNotifications = notificationDao.getAll(uid)
                if (localNotifications.isNotEmpty()) {
                    callback(Result.success(localNotifications.map { it.toDomain() }))
                } else {
                    notificationDataSource.getNotifications(uid) { result ->
                        result.onSuccess { dtos ->
                            val domainNotifications = dtos.map { it.toDomain() }
                            // Cache locally
                            CoroutineScope(Dispatchers.IO).launch {
                                notificationDao.insertAll(domainNotifications.map { it.toEntity(uid) })
                            }
                            callback(Result.success(domainNotifications))
                        }.onFailure {
                            callback(Result.failure(it))
                        }
                    }
                }
            } catch (e: Exception) {
                callback(Result.failure(e))
            }
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
