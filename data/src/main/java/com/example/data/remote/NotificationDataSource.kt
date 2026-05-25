package com.example.data.remote

import com.example.data.remote.dto.NotificationDto
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.UUID

class NotificationDataSource {


    fun sendCheckInConfirmation(passengerId: String, callback: (Result<Unit>) -> Unit) {

    }

    fun getNotifications(uid: String, callback: (Result<List<NotificationDto>>) -> Unit) {

    }

    fun getUnreadCount(uid: String, callback: (Result<Int>) -> Unit) {

    }

    fun markAsRead(notificationId: String, callback: (Result<Unit>) -> Unit) {

    }

    fun markAllAsRead(uid: String, callback: (Result<Unit>) -> Unit) {

    }
}