package com.example.data.remote

import com.example.data.remote.dto.NotificationDto
import com.example.data.remote.dto.NotificationListResponse
import com.example.data.remote.dto.NotificationResponse
import com.example.data.remote.dto.ReadAllResponse
import com.example.data.remote.dto.RegisterTokenRequest
import com.example.data.remote.retrofit.Endpoint
import retrofit2.Response
import javax.inject.Inject

class NotificationDataSource @Inject constructor(
    private val endpoint: Endpoint
) {
    suspend fun getNotifications(): NotificationListResponse {
        return endpoint.getNotifications()
    }

    suspend fun markAsRead(notificationId: String): NotificationResponse {
        return endpoint.markAsRead(notificationId)
    }

    suspend fun markAllAsRead(): ReadAllResponse {
        return endpoint.markAllAsRead()
    }

    suspend fun registerToken(token: String): Response<Unit> {
        return endpoint.registerToken(RegisterTokenRequest(token))
    }
}
