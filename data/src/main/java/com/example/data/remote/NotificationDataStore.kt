package com.example.data.remote

import java.sql.PreparedStatement
import java.sql.SQLException
import java.sql.Timestamp
import java.util.UUID

class NotificationDataStore {

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
}
