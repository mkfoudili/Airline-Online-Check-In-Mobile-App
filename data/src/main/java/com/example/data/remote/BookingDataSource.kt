package com.example.data.remote

import com.example.data.remote.dto.BookingDto
import com.example.data.remote.dto.PassengerDto
import com.example.domain.model.CheckInStatus
import java.sql.ResultSet
import java.sql.SQLException

class BookingDataSource {

    /**
     * Fetches a booking by its PNR and the last name of the traveler.
     */
    fun getBooking(pnr: String, lastName: String, callback: (Result<BookingDto>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM BOOKINGS WHERE pnr = ? AND lastName = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, pnr)
                    preparedStatement.setString(2, lastName)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        val booking = BookingDto(
                            bookingId = resultSet.getString("bookingId"),
                            uid = resultSet.getString("uid"),
                            pnr = resultSet.getString("pnr"),
                            lastName = resultSet.getString("lastName"),
                            status = CheckInStatus.valueOf(resultSet.getString("status")),
                            checkinDeadline = resultSet.getTimestamp("checkinDeadline"),
                            createdAt = resultSet.getTimestamp("createdAt")
                        )
                        callback(Result.success(booking))
                    } else {
                        callback(Result.failure(Exception("Booking not found")))
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
     * Fetches all bookings associated with a specific user ID.
     */
    fun getBookingsByUid(uid: String, callback: (Result<List<BookingDto>>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM BOOKINGS WHERE uid = ? ORDER BY createdAt DESC"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, uid)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    val bookings = mutableListOf<BookingDto>()
                    while (resultSet.next()) {
                        val booking = BookingDto(
                            bookingId = resultSet.getString("bookingId"),
                            uid = resultSet.getString("uid"),
                            pnr = resultSet.getString("pnr"),
                            lastName = resultSet.getString("lastName"),
                            status = CheckInStatus.valueOf(resultSet.getString("status")),
                            checkinDeadline = resultSet.getTimestamp("checkinDeadline"),
                            createdAt = resultSet.getTimestamp("createdAt")
                        )
                        bookings.add(booking)
                    }
                    callback(Result.success(bookings))
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
     * Fetches all passengers associated with a specific booking ID.
     */
    fun getPassengersByBookingId(bookingId: String, callback: (Result<List<PassengerDto>>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM PASSENGERS WHERE bookingId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, bookingId)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    val passengers = mutableListOf<PassengerDto>()
                    while (resultSet.next()) {
                        val passenger = PassengerDto(
                            passengerId = resultSet.getString("passengerId"),
                            bookingId = resultSet.getString("bookingId"),
                            uid = resultSet.getString("uid"),
                            firstName = resultSet.getString("firstName"),
                            lastName = resultSet.getString("lastName"),
                            passportNumber = resultSet.getString("passportNumber"),
                            nationality = resultSet.getString("nationality"),
                            dateOfBirth = resultSet.getString("dateOfBirth"),
                            seatNumber = resultSet.getString("seatNumber"),
                            checkinStatus = resultSet.getString("checkinStatus")
                        )
                        passengers.add(passenger)
                    }
                    callback(Result.success(passengers))
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
