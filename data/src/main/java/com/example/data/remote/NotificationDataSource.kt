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

    fun getNotifications(uid: String, callback: (Result<List<NotificationDto>>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM NOTIFICATIONS WHERE uid = ? ORDER BY createdAt DESC"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, uid)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    val notifications = mutableListOf<NotificationDto>()
                    while (resultSet.next()) {
                        val notification = NotificationDto(
                            notificationId = resultSet.getString("notificationId"),
                            uid = resultSet.getString("uid"),
                            passengerId = resultSet.getString("passengerId"),
                            flightId = resultSet.getString("flightId"),
                            type = resultSet.getString("type"),
                            title = resultSet.getString("title"),
                            body = resultSet.getString("body"),
                            isRead = resultSet.getBoolean("isRead"),
                            createdAt = resultSet.getTimestamp("createdAt")
                        )
                        notifications.add(notification)
                    }
                    callback(Result.success(notifications))
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Could not connect to database")))
            }
        }
    }

    suspend fun markAllAsRead(): ReadAllResponse {
        return endpoint.markAllAsRead()
    }

    suspend fun registerToken(token: String): Response<Unit> {
        return endpoint.registerToken(RegisterTokenRequest(token))
    }
}
