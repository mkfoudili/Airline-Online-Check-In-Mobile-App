package com.example.check_in_mobile_app.presentation.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.lifecycle.lifecycleScope
import com.example.check_in_mobile_app.presentation.checkin.CheckInActivity
import com.example.check_in_mobile_app.presentation.main.profile.ProfileUiAction
import com.example.check_in_mobile_app.presentation.navigation.MainNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.utils.LanguagePreferences
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

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
}