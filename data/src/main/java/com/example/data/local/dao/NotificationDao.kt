package com.example.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.local.entity.NotificationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NotificationDao {

    @Query("SELECT * FROM notifications WHERE uid = :uid")
    suspend fun getAll(uid: String): List<NotificationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: NotificationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entities: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE notificationId = :notificationId")
    suspend fun markAsRead(notificationId: String)

    @Query("UPDATE notifications SET isRead = 1 WHERE uid = :uid")
    suspend fun markAllAsRead(uid: String)

    @Query("SELECT COUNT(*) FROM notifications WHERE uid = :uid AND isRead = 0")
    fun getUnreadCount(uid: String): Flow<Int>

    @Query("DELETE FROM notifications")
    suspend fun deleteAll()
}
