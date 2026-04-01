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
     * Fetches a flight by its ID.
     */
    fun getFlightById(flightId: String, callback: (Result<FlightDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM FLIGHTS WHERE flightId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, flightId)

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
                        callback(Result.failure(Exception("Flight with ID $flightId not found")))
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
