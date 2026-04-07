package com.example.data.mapper

import com.example.data.local.entity.NotificationEntity
import com.example.data.remote.dto.NotificationDto
import com.example.domain.model.Notification
import com.example.domain.model.NotificationType

fun NotificationDto.toDomain(): Notification {
    return Notification(
        notificationId = this.notificationId,
        passengerId = this.passengerId ?: "",
        title = this.title ?: "",
        body = this.body ?: "",
        type = try {
            NotificationType.valueOf(this.type ?: "DELAY")
        } catch (e: Exception) {
            NotificationType.DELAY
        },
        isRead = this.isRead,
        createdAt = this.createdAt?.time ?: 0L
    )
}

fun NotificationEntity.toDomain(): Notification {
    return Notification(
        notificationId = this.notificationId,
        passengerId = this.passengerId ?: "",
        title = this.title ?: "",
        body = this.body ?: "",
        type = try {
            NotificationType.valueOf(this.type ?: "DELAY")
        } catch (e: Exception) {
            NotificationType.DELAY
        },
        isRead = this.isRead,
        createdAt = this.createdAt ?: 0L
    )
}

fun Notification.toEntity(uid: String): NotificationEntity {
    return NotificationEntity(
        notificationId = this.notificationId,
        uid = uid,
        passengerId = this.passengerId,
        flightId = null,
        type = this.type.name,
        title = this.title,
        body = this.body,
        isRead = this.isRead,
        createdAt = this.createdAt
    )
}
