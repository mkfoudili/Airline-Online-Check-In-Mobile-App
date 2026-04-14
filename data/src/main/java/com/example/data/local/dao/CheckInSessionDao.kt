package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.local.entity.CheckInSessionEntity

@Dao
interface CheckInSessionDao {

    @Query("SELECT * FROM checkin_sessions WHERE sessionId = :sessionId")
    suspend fun getSession(sessionId: String): CheckInSessionEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(entity: CheckInSessionEntity)

    @Update
    suspend fun updateSession(entity: CheckInSessionEntity)
}
