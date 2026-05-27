package com.example.data.mapper

import com.example.data.local.entity.NotificationEntity
import com.example.data.remote.dto.NotificationDto
import com.example.domain.model.Notification
import com.example.domain.model.NotificationType
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

/**
 * Maps NotificationDto to domain Notification model.
 */
fun NotificationDto.toDomain(): Notification {
    return Notification(
        notificationId = this.notificationId,
        passengerId = this.passengerId ?: "",
        title = this.title,
        body = this.body,
        type = mapToNotificationType(this.type),
        isRead = this.isRead,
        createdAt = parseIsoDate(this.createdAt)
    )
}

/**
 * Maps NotificationDto to local Entity.
 */
fun NotificationDto.toEntity(uid: String): NotificationEntity {
    return NotificationEntity(
        notificationId = this.notificationId,
        uid = uid,
        passengerId = this.passengerId,
        flightId = this.flightId,
        type = this.type,
        title = this.title,
        body = this.body,
        isRead = this.isRead,
        createdAt = parseIsoDate(this.createdAt)
    )
}

/**
 * Maps NotificationEntity to domain model.
 */
fun NotificationEntity.toDomain(): Notification {
    return Notification(
        notificationId = this.notificationId,
        passengerId = this.passengerId ?: "",
        title = this.title ?: "",
        body = this.body ?: "",
        type = mapToNotificationType(this.type ?: ""),
        isRead = this.isRead,
        createdAt = this.createdAt ?: 0L
    )
}

/**
 * Safely maps the backend type string to NotificationType enum.
 */
private fun mapToNotificationType(type: String): NotificationType {
    return try {
        when (type.uppercase()) {
            "CHECK_IN" -> NotificationType.CHECK_IN_CONFIRMATION
            "BOARDING" -> NotificationType.BOARDING_REMINDER
            "FLIGHT_STATUS" -> NotificationType.FLIGHT_STATUS_UPDATE
            "GATE_CHANGE" -> NotificationType.GATE_CHANGE
            "DELAY" -> NotificationType.DELAY
            "WELCOME" -> NotificationType.OTHER
            else -> NotificationType.valueOf(type.uppercase())
        }
    } catch (e: Exception) {
        NotificationType.OTHER
    }
}

/**
 * Parses ISO 8601 date string to Long timestamp.
 */
private fun parseIsoDate(dateString: String): Long {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        sdf.timeZone = TimeZone.getTimeZone("UTC")
        sdf.parse(dateString)?.time ?: System.currentTimeMillis()
    } catch (e: Exception) {
        try {
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(dateString)?.time 
                ?: System.currentTimeMillis()
        } catch (e2: Exception) {
            System.currentTimeMillis()
        }
    }
}
