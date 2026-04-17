package com.example.domain.model

data class Notification(
    val notificationId: String,
    val passengerId: String,
    val title: String,
    val body: String,
    val type: NotificationType,
    val isRead: Boolean,
    val createdAt: Long
)
