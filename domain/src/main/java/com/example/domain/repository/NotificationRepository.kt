package com.example.domain.repository

import com.example.domain.model.Notification

interface NotificationRepository {
    fun getNotifications(uid: String, callback: (Result<List<Notification>>) -> Unit)
    fun markAsRead(notificationId: String, callback: (Result<Unit>) -> Unit)
    fun markAllAsRead(uid: String, callback: (Result<Unit>) -> Unit)
    fun getUnreadCount(uid: String, callback: (Result<Int>) -> Unit)
}