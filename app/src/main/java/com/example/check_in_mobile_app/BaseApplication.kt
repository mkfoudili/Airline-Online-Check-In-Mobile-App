package com.example.check_in_mobile_app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class BaseApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        val getSavedLanguageUseCase = AppContainer.provideGetSavedLanguageUseCase(this)
        MainScope().launch {
            val langTag = getSavedLanguageUseCase().first() ?: "en"
            val locales = LocaleListCompat.forLanguageTags(langTag)
            AppCompatDelegate.setApplicationLocales(locales)
        }
    }
}