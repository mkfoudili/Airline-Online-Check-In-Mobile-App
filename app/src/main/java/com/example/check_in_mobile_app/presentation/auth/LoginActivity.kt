package com.example.check_in_mobile_app.presentation.auth

import android.content.Context
import com.example.check_in_mobile_app.presentation.navigation.LoginNavGraph
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.check_in_mobile_app.utils.LanguagePreferences

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        // Force sync the language from SharedPreferences to AppCompatDelegate on the first screen
        val savedLang = LanguagePreferences.getSavedLanguage(this)
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.isEmpty || currentLocales.toLanguageTags() != savedLang) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(savedLang))
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckInMobileAppTheme {
                LoginNavGraph(
                    onLoginSuccess = { goToMain() }
                )
            }
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}