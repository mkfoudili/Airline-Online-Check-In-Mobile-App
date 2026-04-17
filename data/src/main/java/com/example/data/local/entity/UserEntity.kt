package com.example.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val uid: String,
    val email: String?,
    val displayName: String?,
    val phoneNumber: String?,
    val photoUrl: String?,
    val provider: String?,
    val createdAt: Long?,
    val lastLogin: Long?
)
