package com.example.data.remote

import java.sql.ResultSet
import java.sql.SQLException

class CheckInDataSource {

    /**
     * Updates the seat availability and assigns it to a passenger.
     * Also updates the passenger's check-in status and seat number.
     */
    fun updateSeat(seatId: String, passengerId: String, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    connection.autoCommit = false // Begin transaction

                    // 1. Update the SEAT_MAP to mark it as occupied
                    val updateSeatSql = "UPDATE SEAT_MAP SET isAvailable = ?, occupiedBy = ? WHERE seatId = ?"
                    val seatStmt = connection.prepareStatement(updateSeatSql)
                    seatStmt.setBoolean(1, false)
                    seatStmt.setString(2, passengerId)
                    seatStmt.setString(3, seatId)
                    val rowsAffected = seatStmt.executeUpdate()

                    if (rowsAffected > 0) {
                        // 2. Fetch the seat number to sync with the PASSENGERS table
                        val selectSeatSql = "SELECT seatNumber FROM SEAT_MAP WHERE seatId = ?"
                        val selectStmt = connection.prepareStatement(selectSeatSql)
                        selectStmt.setString(1, seatId)
                        val resultSet = selectStmt.executeQuery()

                        if (resultSet.next()) {
                            val seatNumber = resultSet.getString("seatNumber")

                            // 3. Update the PASSENGERS table with the new seat and status
                            val updatePassengerSql = "UPDATE PASSENGERS SET seatNumber = ?, checkinStatus = ? WHERE passengerId = ?"
                            val passengerStmt = connection.prepareStatement(updatePassengerSql)
                            passengerStmt.setString(1, seatNumber)
                            passengerStmt.setString(2, "CHECKED_IN")
                            passengerStmt.setString(3, passengerId)
                            passengerStmt.executeUpdate()
                        }

                        connection.commit()
                        callback(Result.success(Unit))
                    } else {
                        connection.rollback()
                        callback(Result.failure(Exception("Seat assignment failed: Seat not found or already taken.")))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    try { connection.rollback() } catch (rollbackEx: SQLException) { rollbackEx.printStackTrace() }
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Database connection failed")))
            }
        }
    }

    /**
     * Creates a new check-in session in the database.
     */
    fun createSession(session: CheckinSessionDto, callback: (Result<CheckinSessionDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = """
                        INSERT INTO CHECKIN_SESSIONS (
                            sessionId, passengerId, bookingId, currentStep, 
                            passportScanUrl, ocrValidation, baggageDeclaration, 
                            specialRequests, completedAt
                        ) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
                    """.trimIndent()
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, session.sessionId)
                    preparedStatement.setString(2, session.passengerId)
                    preparedStatement.setString(3, session.bookingId)
                    preparedStatement.setString(4, session.currentStep)
                    preparedStatement.setString(5, session.passportScanUrl)
                    preparedStatement.setString(6, session.ocrValidation)
                    preparedStatement.setString(7, session.baggageDeclaration)
                    preparedStatement.setString(8, session.specialRequests)
                    preparedStatement.setTimestamp(9, session.completedAt)

                    val rowsInserted = preparedStatement.executeUpdate()
                    if (rowsInserted > 0) {
                        callback(Result.success(session))
                    } else {
                        callback(Result.failure(Exception("Failed to create session")))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Database connection failed")))
            }
        }
    }

    /**
     * Retrieves a check-in session by its ID.
     */
    fun getSession(sessionId: String, callback: (Result<CheckinSessionDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM CHECKIN_SESSIONS WHERE sessionId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, sessionId)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        val session = CheckinSessionDto(
                            sessionId = resultSet.getString("sessionId"),
                            passengerId = resultSet.getString("passengerId"),
                            bookingId = resultSet.getString("bookingId"),
                            currentStep = resultSet.getString("currentStep"),
                            passportScanUrl = resultSet.getString("passportScanUrl"),
                            ocrValidation = resultSet.getString("ocrValidation"),
                            baggageDeclaration = resultSet.getString("baggageDeclaration"),
                            specialRequests = resultSet.getString("specialRequests"),
                            completedAt = resultSet.getTimestamp("completedAt")
                        )
                        callback(Result.success(session))
                    } else {
                        callback(Result.failure(Exception("Session not found")))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Database connection failed")))
            }
        }
    }

    /**
     * Updates an existing check-in session.
     */
    fun updateSession(session: CheckinSessionDto, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = """
                        UPDATE CHECKIN_SESSIONS SET 
                            currentStep = ?, passportScanUrl = ?, ocrValidation = ?, 
                            baggageDeclaration = ?, specialRequests = ?, completedAt = ?
                        WHERE sessionId = ?
                    """.trimIndent()
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, session.currentStep)
                    preparedStatement.setString(2, session.passportScanUrl)
                    preparedStatement.setString(3, session.ocrValidation)
                    preparedStatement.setString(4, session.baggageDeclaration)
                    preparedStatement.setString(5, session.specialRequests)
                    preparedStatement.setTimestamp(6, session.completedAt)
                    preparedStatement.setString(7, session.sessionId)

                    val rowsUpdated = preparedStatement.executeUpdate()
                    if (rowsUpdated > 0) {
                        callback(Result.success(Unit))
                    } else {
                        callback(Result.failure(Exception("Failed to update session: Session not found")))
                    }
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Database connection failed")))
            }
        }
    }
}
