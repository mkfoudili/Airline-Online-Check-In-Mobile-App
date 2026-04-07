package com.example.data.mapper

import com.example.data.remote.dto.UserDto
import com.example.domain.model.User

fun UserDto.toDomain(): User {
    return User(
        uid = this.uid,
        email = this.email ?: "",
        displayName = this.displayName,
        phoneNumber = this.phoneNumber,
        provider = this.provider
    )
}
