package com.example.domain.repository

import com.example.domain.model.Profile

interface ProfileRepository {
    fun getProfile(callback: (Result<Profile>) -> Unit)
    fun updateProfile(profile: Profile, callback: (Result<Unit>) -> Unit)
    fun changePassword(currentPassword: String, newPassword: String, callback: (Result<Unit>) -> Unit)
    fun changeEmail(newEmail: String, callback: (Result<Unit>) -> Unit)
    fun changeAvatar(avatarUri: String, callback: (Result<Unit>) -> Unit)
}