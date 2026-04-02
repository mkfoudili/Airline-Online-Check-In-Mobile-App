package com.example.data.remote

import com.example.domain.validation.RegistrationRequest
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.SQLException
import java.sql.Timestamp

class AuthDataSource {

    /**
     * Registers a new user in the MySQL database.
     */
    fun register(request: RegistrationRequest, callback: (UserDto?) -> Unit) {
        MySqlHelper.getConnection { connection ->
            var user: UserDto? = null
            if (connection != null) {
                try {
                    val sql = """
                        INSERT INTO USERS (uid, email, displayName, phoneNumber, createdAt, lastLogin) 
                        VALUES (?, ?, ?, ?, ?, ?)
                    """.trimIndent()
                    
                    val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                    val currentTime = Timestamp(System.currentTimeMillis())
                    
                    preparedStatement.setString(1, request.uid)
                    preparedStatement.setString(2, request.email)
                    preparedStatement.setString(3, request.displayName)
                    preparedStatement.setString(4, request.phoneNumber)
                    preparedStatement.setTimestamp(5, currentTime)
                    preparedStatement.setTimestamp(6, currentTime)

                    val rowsInserted = preparedStatement.executeUpdate()
                    if (rowsInserted > 0) {
                        user = UserDto(
                            uid = request.uid,
                            email = request.email,
                            displayName = request.displayName,
                            phoneNumber = request.phoneNumber,
                            photoUrl = null,
                            provider = null,
                            createdAt = currentTime,
                            lastLogin = currentTime
                        )
                    }
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
            callback(user)
        }
    }

    /**
     * Logs in a user by checking email and a hypothetical password field.
     */
    fun login(email: String, password: String, callback: (UserDto?) -> Unit) {
        MySqlHelper.getConnection { connection ->
            var user: UserDto? = null
            if (connection != null) {
                try {
                    val sql = "SELECT * FROM USERS WHERE email = ? AND password = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, email)
                    preparedStatement.setString(2, password)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        user = UserDto(
                            uid = resultSet.getString("uid"),
                            email = resultSet.getString("email"),
                            displayName = resultSet.getString("displayName"),
                            phoneNumber = resultSet.getString("phoneNumber"),
                            photoUrl = resultSet.getString("photoUrl"),
                            provider = resultSet.getString("provider"),
                            createdAt = resultSet.getTimestamp("createdAt"),
                            lastLogin = resultSet.getTimestamp("lastLogin")
                        )
                        
                        // Update last login
                        val updateSql = "UPDATE USERS SET lastLogin = ? WHERE uid = ?"
                        val updateStmt = connection.prepareStatement(updateSql)
                        updateStmt.setTimestamp(1, Timestamp(System.currentTimeMillis()))
                        updateStmt.setString(2, user.uid)
                        updateStmt.executeUpdate()
                    }
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
            callback(user)
        }
    }

    /**
     * Checks if an email already exists in the USERS table.
     */
    fun emailExists(email: String, callback: (Boolean) -> Unit) {
        MySqlHelper.getConnection { connection ->
            var exists = false
            if (connection != null) {
                try {
                    val sql = "SELECT COUNT(*) FROM USERS WHERE email = ?"
                    val preparedStatement: PreparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, email)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        exists = resultSet.getInt(1) > 0
                    }
                    connection.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                }
            }
            callback(exists)
        }
    }
}
