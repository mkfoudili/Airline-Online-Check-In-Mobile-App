package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val notificationId: String,
    val uid: String?,
    val passengerId: String?,
    val flightId: String?,
    val type: String?,
    val title: String?,
    val body: String?,
    val isRead: Boolean,
    val createdAt: Long?
)