package com.example.check_in_mobile_app.utils

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

object LanguagePreferences {

    private const val PREFS_NAME = "language_prefs"
    private const val KEY_LANGUAGE = "selected_language"
    private const val DEFAULT_LANGUAGE = "en"

   
    fun saveLanguage(context: Context, languageCode: String) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putString(KEY_LANGUAGE, languageCode)
            .commit()
    }

    /** Read the saved language tag from SharedPreferences. */
    fun getSavedLanguage(context: Context): String {
        return try {
            context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getString(KEY_LANGUAGE, DEFAULT_LANGUAGE) ?: DEFAULT_LANGUAGE
        } catch (e: Exception) {
            DEFAULT_LANGUAGE
        }
    }

    /** Convert a display name ("English", "French", "Arabic") to a BCP-47 tag. */
    fun displayNameToCode(displayName: String): String = when (displayName) {
        "French"  -> "fr"
        "Arabic"  -> "ar"
        else      -> "en"
    }

    fun codeToDisplayName(code: String): String = when (code) {
        "fr" -> "French"
        "ar" -> "Arabic"
        else -> "English"
    }
}
