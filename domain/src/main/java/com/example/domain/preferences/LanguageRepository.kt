package com.example.domain.preferences

import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    val appLanguageFlow: Flow<String?>
    suspend fun setAppLanguage(lang: String)
}