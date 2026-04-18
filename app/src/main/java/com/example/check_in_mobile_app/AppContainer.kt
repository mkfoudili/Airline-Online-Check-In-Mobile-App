package com.example.check_in_mobile_app

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.preferences.LanguageRepository

private val Context.dataStore by preferencesDataStore("user_prefs")

object AppContainer {

    fun provideLanguageRepository(context: Context): LanguageRepository {
        return UserPreferencesRepository(context.dataStore)
    }
}