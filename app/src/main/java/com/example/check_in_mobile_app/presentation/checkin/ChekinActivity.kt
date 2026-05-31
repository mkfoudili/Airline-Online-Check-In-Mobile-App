package com.example.check_in_mobile_app.presentation.checkin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import com.example.check_in_mobile_app.presentation.navigation.CheckInNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.utils.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val bookingRef  = intent.getStringExtra("booking_ref")  ?: ""
        val bookingId   = intent.getStringExtra("booking_id")   ?: ""
        val passengerId = intent.getStringExtra("passenger_id") ?: ""

        setContent {
            val context = LocalContext.current
            val systemDark = isSystemInDarkTheme()
            val darkThemeEnabled = remember {
                mutableStateOf(ThemePreferences.isDarkModeEnabled(context) ?: systemDark)
            }

            CheckInMobileAppTheme(darkTheme = darkThemeEnabled.value) {
                CheckInNavGraph(
                    bookingRef          = bookingRef,
                    bookingId           = bookingId,
                    passengerId         = passengerId,
                    onBackFromFirstStep = { finish() },
                    onCheckInComplete   = {
                        setResult(RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}