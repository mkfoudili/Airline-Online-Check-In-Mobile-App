package com.example.data.remote

import java.sql.Connection
import java.sql.DriverManager
import java.util.concurrent.Executors

object MySqlHelper {
    private const val DB_URL = "jdbc:mysql://127.0.0.1:3306/android"
    private const val USER = "root"
    private const val PASS = ""

    private val executor = Executors.newSingleThreadExecutor()

    fun getConnection(callback: (Connection?) -> Unit) {
        executor.execute {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver")
                val connection = DriverManager.getConnection(DB_URL, USER, PASS)
                callback(connection)
            } catch (e: Exception) {
                e.printStackTrace()
                callback(null)
            }
        }
    }
}