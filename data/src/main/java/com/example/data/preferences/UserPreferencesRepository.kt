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

    val isLoggedInFlow: Flow<Boolean> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.IS_LOGGED_IN] ?: false
        }

    val userNameFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_NAME]
        }

    val userEmailFlow: Flow<String?> =
        dataStore.data.map { preferences ->
            preferences[PreferencesKeys.USER_EMAIL]
        }

    suspend fun saveUser(name: String, email: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.USER_NAME]    = name
            preferences[PreferencesKeys.USER_EMAIL]   = email
            preferences[PreferencesKeys.IS_LOGGED_IN] = true
        }
    }

    suspend fun clearUser() {
        dataStore.edit { preferences ->
            preferences.remove(PreferencesKeys.USER_NAME)
            preferences.remove(PreferencesKeys.USER_EMAIL)
            preferences[PreferencesKeys.IS_LOGGED_IN] = false
        }
    }
}
