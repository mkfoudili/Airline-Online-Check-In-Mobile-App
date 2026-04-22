package com.example.check_in_mobile_app.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.check_in_mobile_app.presentation.checkin.CheckInActivity
import com.example.check_in_mobile_app.presentation.navigation.MainNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme

class MainActivity : ComponentActivity() {
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