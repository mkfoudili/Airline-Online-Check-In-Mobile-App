package com.example.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.example.domain.preferences.LanguageRepository

class UserPreferencesRepository(private val dataStore: DataStore<Preferences>) : LanguageRepository{
    override val appLanguageFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE]
        }

    override suspend fun setAppLanguage(lang: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.APP_LANGUAGE] = lang
        }
    }
}
