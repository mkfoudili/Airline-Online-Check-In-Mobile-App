package com.example.domain.usecase.language

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.domain.preferences.LanguageRepository

class ChangeLanguageUseCase(
    private val repo: LanguageRepository
) {
    suspend operator fun invoke(lang: String) {
        repo.setAppLanguage(lang)

        val locales = LocaleListCompat.forLanguageTags(lang)
        AppCompatDelegate.setApplicationLocales(locales)
    }
}