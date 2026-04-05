package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey val notificationId: String,
    val uid: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean = false
)
// + passengerId + flightId  + type + body
// - message - timestamp