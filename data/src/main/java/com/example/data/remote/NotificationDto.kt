package com.example.data.remote

import java.sql.Timestamp

data class NotificationDto(
    val notificationId: String,
    val uid: String?,
    val passengerId: String?,
    val flightId: String?,
    val type: String?,
    val title: String?,
    val body: String?,
    val isRead: Boolean,
    val createdAt: Timestamp?
)
