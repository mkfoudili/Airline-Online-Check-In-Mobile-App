package com.example.data.remote

import java.sql.ResultSet
import java.sql.SQLException

class FlightDataSource {

    /**
     * Fetches the itinerary for a given flight number.
     * Returns a Result<FlightDto> indicating success or failure.
     */
    fun getItinerary(flightNumber: String, callback: (Result<FlightDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM FLIGHTS WHERE flightNumber = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, flightNumber)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        val flight = FlightDto(
                            flightId = resultSet.getString("flightId"),
                            flightNumber = resultSet.getString("flightNumber"),
                            origin = resultSet.getString("origin"),
                            destination = resultSet.getString("destination"),
                            departureTime = resultSet.getTimestamp("departureTime"),
                            arrivalTime = resultSet.getTimestamp("arrivalTime"),
                            aircraftType = resultSet.getString("aircraftType"),
                            status = resultSet.getString("status")
                        )
                        callback(Result.success(flight))
                    } else {
                        callback(Result.failure(Exception("Flight not found")))
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
     * Fetches the seat map for a given flight ID.
     * Returns a Result<List<SeatMapDto>> indicating success or failure.
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
}
