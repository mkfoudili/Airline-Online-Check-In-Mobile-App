package com.example.check_in_mobile_app.presentation.checkin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.check_in_mobile_app.presentation.navigation.CheckInNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme

class CheckInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val bookingRef = intent.getStringExtra("booking_ref") ?: ""

        setContent {
            CheckInMobileAppTheme {
                CheckInNavGraph(
                    bookingRef = bookingRef,
                    onCheckInComplete = { finish() }
                )
            }
        }
    }
}