package com.example.data.remote

import com.example.data.remote.dto.NotificationDto
import com.example.data.remote.dto.ReadAllResponse
import com.example.data.remote.dto.RegisterTokenRequest
import com.example.data.remote.retrofit.Endpoint
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val endpoint: Endpoint
) {
    suspend fun getNotifications(): List<NotificationDto> {
        return endpoint.getNotifications()
    }

    suspend fun markAsRead(notificationId: String): NotificationDto {
        return endpoint.markAsRead(notificationId)
    }

    suspend fun markAllAsRead(): ReadAllResponse {
        return endpoint.markAllAsRead()
    }

    suspend fun registerToken(token: String) {
        endpoint.registerToken(RegisterTokenRequest(token))
    }
}
