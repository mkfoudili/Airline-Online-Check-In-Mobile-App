package com.example.data.remote


class StorageDataSource {

    /**
     * Returns Boarding pass url
     * Note: add a boarding pass generator to pdf later
     */
    fun getBoardingPassPdf(passId: String, callback: (Result<String>) -> Unit) {
        MySqlHelper.getConnection { connection ->
            if (connection != null) {
                try {
                    val sql = "SELECT passId FROM BOARDING_PASSES WHERE passId = ?"
                    val preparedStatement = connection.prepareStatement(sql)
                    preparedStatement.setString(1, passId)

                    val resultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        // Pdf endpoint example
                        val pdfUrl = "https://api.airline-app.com/v1/generate-pdf/boarding-pass/$passId"
                        callback(Result.success(pdfUrl))
                    } else {
                        callback(Result.failure(Exception("Boarding pass not found")))
                    }
                    connection.close()
                } catch (e: Exception) {
                    callback(Result.failure(e))
                }
            } else {
                callback(Result.failure(Exception("Database connection failed")))
            }
        }
    }
}