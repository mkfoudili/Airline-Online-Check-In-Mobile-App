package com.example.data.repository

import com.example.domain.model.Notification
import com.example.domain.repository.NotificationRepository

class NotificationRepositoryImpl(
) : NotificationRepository {

    override fun getNotifications(uid: String, callback: (Result<List<Notification>>) -> Unit) {
        // TODO
    }

    override fun getUnreadCount(uid: String, callback: (Result<Int>) -> Unit) {
        // TODO
    }

    override fun markAllAsRead(uid: String, callback: (Result<Unit>) -> Unit) {
        // TODO
    }

    override fun markAsRead(notificationId: String, callback: (Result<Unit>) -> Unit) {
        // TODO
    }
}
