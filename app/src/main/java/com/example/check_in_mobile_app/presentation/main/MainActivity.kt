package com.example.check_in_mobile_app.presentation.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.lifecycleScope
import com.example.check_in_mobile_app.data.NotificationManager
import com.example.check_in_mobile_app.presentation.auth.AuthViewModel
import com.example.check_in_mobile_app.presentation.auth.LoginActivity
import com.example.check_in_mobile_app.presentation.checkin.CheckInActivity
import com.example.check_in_mobile_app.presentation.navigation.MainNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.check_in_mobile_app.utils.ThemePreferences
import dagger.hilt.android.AndroidEntryPoint
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var notificationManager: NotificationManager

    private val authViewModel: AuthViewModel by viewModels()

    private val navigateToHomeAfterCheckIn = mutableStateOf(false)

    private val checkInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            navigateToHomeAfterCheckIn.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            notificationManager.fetchInitialStatus()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Log.d("FCM", "TOKEN = $token")
        }
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            val systemDark = isSystemInDarkTheme()
            val darkThemeEnabled = remember {
                mutableStateOf(ThemePreferences.isDarkModeEnabled(context) ?: systemDark)
            }

            CheckInMobileAppTheme(darkTheme = darkThemeEnabled.value) {
                MainNavGraph(
                    isDarkThemeEnabled = darkThemeEnabled.value,
                    onThemeChanged = { isDark ->
                        ThemePreferences.saveDarkModeEnabled(context, isDark)
                        darkThemeEnabled.value = isDark
                    },
                    onCheckInClick = { bookingRef, passengerId, bookingId ->
                        Intent(this, CheckInActivity::class.java).also {
                            it.putExtra("booking_ref",  bookingRef)
                            it.putExtra("passenger_id", passengerId)
                            it.putExtra("booking_id",   bookingId)
                            checkInLauncher.launch(it)
                        }
                    },
                    onLogout = {
                        authViewModel.onLogout()
                        Intent(this, LoginActivity::class.java).also {
                            it.putExtra("FROM_LOGOUT", true)
                            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(it)
                        }
                    },
                    navigateToHome          = navigateToHomeAfterCheckIn,
                    onNavigateToHomeHandled = { navigateToHomeAfterCheckIn.value = false }
                )
            }
        }
    }
}
