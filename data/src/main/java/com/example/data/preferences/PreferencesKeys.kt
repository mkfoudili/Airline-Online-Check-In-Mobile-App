package com.example.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferencesKeys {
    val APP_LANGUAGE = stringPreferencesKey("app_language")
    val IS_DARK_MODE = booleanPreferencesKey("is_dark_mode")
    val USER_NAME    = stringPreferencesKey("user_name")
    val USER_EMAIL   = stringPreferencesKey("user_email")
    val USER_ID      = stringPreferencesKey("user_id")
    val USER_TOKEN   = stringPreferencesKey("user_token")
    val IS_LOGGED_IN = booleanPreferencesKey("is_logged_in")
}
