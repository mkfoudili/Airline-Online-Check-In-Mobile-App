package com.example.data.remote

import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp
import java.util.UUID

class NotificationDataSource {

    /**
     * Sends a check-in confirmation notification by inserting a record into the NOTIFICATIONS table.
     * Fetches the associated user ID (uid) from the PASSENGERS table first.
     */
    fun sendCheckInConfirmation(passengerId: String, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    // 1. Identify the user associated with this passenger
                    val findUserSql = "SELECT uid FROM PASSENGERS WHERE passengerId = ?"
                    val findUserStmt = connection.prepareStatement(findUserSql)
                    findUserStmt.setString(1, passengerId)
                    val resultSet = findUserStmt.executeQuery()

                    if (resultSet.next()) {
                        val uid = resultSet.getString("uid")

                        // 2. Insert the notification record
                        val insertSql = """
                            INSERT INTO NOTIFICATIONS (
                                notificationId, uid, passengerId, type, title, body, isRead, createdAt
                            ) VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                        """.trimIndent()

                        val insertStmt: PreparedStatement = connection.prepareStatement(insertSql)
                        val currentTime = Timestamp(System.currentTimeMillis())

                        insertStmt.setString(1, UUID.randomUUID().toString())
                        insertStmt.setString(2, uid)
                        insertStmt.setString(3, passengerId)
                        insertStmt.setString(4, "CHECK_IN_CONFIRMATION")
                        insertStmt.setString(5, "Check-in Successful")
                        insertStmt.setString(6, "Your check-in has been confirmed. You can now view your boarding pass.")
                        insertStmt.setBoolean(7, false) // isRead = false
                        insertStmt.setTimestamp(8, currentTime)

                        insertStmt.executeUpdate()
                        callback(Result.success(Unit))
                    } else {
                        callback(Result.failure(Exception("Passenger with ID ${passengerId} not found.")))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Failed to connect to the database.")))
            }
        }
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

    fun getUnreadCount(uid: String, callback: (Result<Int>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT COUNT(*) FROM NOTIFICATIONS WHERE uid = ? AND isRead = false"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, uid)

                    val resultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        callback(Result.success(resultSet.getInt(1)))
                    } else {
                        callback(Result.success(0))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Could not connect to database")))
            }
        }
    }

    fun markAsRead(notificationId: String, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "UPDATE NOTIFICATIONS SET isRead = true WHERE notificationId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, notificationId)
                    preparedStatement.executeUpdate()
                    callback(Result.success(Unit))
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Could not connect to database")))
            }
        }
    }

    fun markAllAsRead(uid: String, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "UPDATE NOTIFICATIONS SET isRead = true WHERE uid = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, uid)
                    preparedStatement.executeUpdate()
                    callback(Result.success(Unit))
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Could not connect to database")))
            }
        }
    }
}
