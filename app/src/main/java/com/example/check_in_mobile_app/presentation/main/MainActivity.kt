package com.example.check_in_mobile_app.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.check_in_mobile_app.presentation.checkin.CheckInActivity
import com.example.check_in_mobile_app.presentation.main.profile.ProfileUiAction
import com.example.check_in_mobile_app.presentation.navigation.MainNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.utils.LangUtil
import com.example.check_in_mobile_app.utils.LanguagePreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat

class MainActivity : AppCompatActivity() {

    // LanguageAction is emitted from ProfileViewModel through the nav graph;
    // we handle it here via a companion callback so the nav graph can pass it up.
    companion object {
        var onLanguageChangeRequest: ((String) -> Unit)? = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Register the callback so the nav graph can trigger a language change
        onLanguageChangeRequest = { languageCode ->
            val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
            AppCompatDelegate.setApplicationLocales(appLocale)
        }

        setContent {
            CheckInMobileAppTheme {
                MainNavGraph(
                    onCheckInClick = { bookingRef ->
                        Intent(this, CheckInActivity::class.java).also {
                            it.putExtra("booking_ref", bookingRef)
                            startActivity(it)
                        }
                    }
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        onLanguageChangeRequest = null
    }
}