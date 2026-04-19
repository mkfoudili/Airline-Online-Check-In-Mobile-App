package com.example.check_in_mobile_app

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.preferences.UserPreferencesRepository
import com.example.domain.preferences.LanguageRepository
import com.example.domain.usecase.language.ChangeLanguageUseCase
import com.example.domain.usecase.language.GetSavedLanguageUseCase

private val Context.dataStore by preferencesDataStore("user_prefs")

object AppContainer {

    fun provideLanguageRepository(context: Context): LanguageRepository {
        return UserPreferencesRepository(context.dataStore)
    }

    fun provideChangeLanguageUseCase(context: Context): ChangeLanguageUseCase {
        return ChangeLanguageUseCase(provideLanguageRepository(context))
    }

    fun provideGetSavedLanguageUseCase(context: Context): GetSavedLanguageUseCase {
        return GetSavedLanguageUseCase(provideLanguageRepository(context))
    }
}
