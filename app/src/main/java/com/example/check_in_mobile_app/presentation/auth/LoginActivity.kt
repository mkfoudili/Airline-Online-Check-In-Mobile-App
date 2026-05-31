package com.example.check_in_mobile_app.presentation.auth

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.check_in_mobile_app.presentation.navigation.LoginNavGraph
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.utils.ThemePreferences
import com.example.check_in_mobile_app.utils.LanguagePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val savedLang = LanguagePreferences.getSavedLanguage(this)
        val currentLocales = AppCompatDelegate.getApplicationLocales()
        if (currentLocales.isEmpty || currentLocales.toLanguageTags() != savedLang) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(savedLang))
        }

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val fromLogout = intent.getBooleanExtra("FROM_LOGOUT", false)

        setContent {
            val context = LocalContext.current
            val systemDark = isSystemInDarkTheme()
            val darkThemeEnabled = remember {
                mutableStateOf(ThemePreferences.isDarkModeEnabled(context) ?: systemDark)
            }

            CheckInMobileAppTheme(darkTheme = darkThemeEnabled.value) {
                LoginNavGraph(
                    viewModel = viewModel,
                    startFromLogout = fromLogout,
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