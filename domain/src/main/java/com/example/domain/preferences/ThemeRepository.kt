package com.example.domain.preferences

import kotlinx.coroutines.flow.Flow

interface ThemeRepository {
    val isDarkModeFlow: Flow<Boolean>
    suspend fun setDarkMode(isDarkMode: Boolean)
}
