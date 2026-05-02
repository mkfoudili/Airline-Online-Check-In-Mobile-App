package com.example.data.remote

import com.example.data.remote.dto.SeatMapDto
import java.sql.ResultSet
import java.sql.SQLException

class SeatDataSource {

    /**
     * Fetches the seat map for a given flight ID.
     */
    fun getSeatMap(flightId: String, callback: (Result<List<SeatMapDto>>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM SEAT_MAP WHERE flightId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, flightId)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    val seats = mutableListOf<SeatMapDto>()
                    while (resultSet.next()) {
                        val seat = SeatMapDto(
                            seatId = resultSet.getString("seatId"),
                            flightId = resultSet.getString("flightId"),
                            seatNumber = resultSet.getString("seatNumber"),
                            seatClass = resultSet.getString("seatClass"),
                            isAvailable = resultSet.getBoolean("isAvailable"),
                            isPremium = resultSet.getBoolean("isPremium"),
                            occupiedBy = resultSet.getString("occupiedBy")
                        )
                        seats.add(seat)
                    }
                    callback(Result.success(seats))
                    connection.close()
                } catch (e: SQLException) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Could not connect to database")))
            }
        }
    }

    /**
     * Reserves a seat for a passenger.
     */
    fun reserveSeat(seatId: String, passengerId: String, callback: (Result<SeatMapDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "UPDATE SEAT_MAP SET isAvailable = false, occupiedBy = ? WHERE seatId = ? AND isAvailable = true"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, passengerId)
                    preparedStatement.setString(2, seatId)

                    val rowsAffected = preparedStatement.executeUpdate()
                    if (rowsAffected > 0) {
                        // Fetch the updated seat to return it
                        val selectSql = "SELECT * FROM SEAT_MAP WHERE seatId = ?"
                        val selectStmt = connection.prepareStatement(selectSql)
                        selectStmt.setString(1, seatId)
                        val resultSet = selectStmt.executeQuery()
                        if (resultSet.next()) {
                            val seat = SeatMapDto(
                                seatId = resultSet.getString("seatId"),
                                flightId = resultSet.getString("flightId"),
                                seatNumber = resultSet.getString("seatNumber"),
                                seatClass = resultSet.getString("seatClass"),
                                isAvailable = resultSet.getBoolean("isAvailable"),
                                isPremium = resultSet.getBoolean("isPremium"),
                                occupiedBy = resultSet.getString("occupiedBy")
                            )
                            callback(Result.success(seat))
                        }
                    } else {
                        callback(Result.failure(Exception("Seat not available or not found")))
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

    /**
     * Releases a reserved seat.
     */
    fun releaseSeat(seatId: String, callback: (Result<Unit>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "UPDATE SEAT_MAP SET isAvailable = true, occupiedBy = NULL WHERE seatId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, seatId)

                    val rowsAffected = preparedStatement.executeUpdate()
                    if (rowsAffected > 0) {
                        callback(Result.success(Unit))
                    } else {
                        callback(Result.failure(Exception("Seat not found")))
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
}