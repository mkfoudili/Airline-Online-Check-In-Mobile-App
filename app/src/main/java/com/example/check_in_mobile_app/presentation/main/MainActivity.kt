package com.example.check_in_mobile_app.presentation.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.mutableStateOf
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.check_in_mobile_app.presentation.checkin.CheckInActivity
import com.example.check_in_mobile_app.presentation.navigation.MainNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme

class MainActivity : AppCompatActivity() {

    private val navigateToHomeAfterCheckIn = mutableStateOf(false)

    private val checkInLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            navigateToHomeAfterCheckIn.value = true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            CheckInMobileAppTheme {
                MainNavGraph(
                    onCheckInClick = { bookingRef ->
                        Intent(this, CheckInActivity::class.java).also {
                            it.putExtra("booking_ref", bookingRef)
                            checkInLauncher.launch(it)
                        }
                    },
                    navigateToHome = navigateToHomeAfterCheckIn,
                    onNavigateToHomeHandled = { navigateToHomeAfterCheckIn.value = false }
                )
            }
        }
    }
}