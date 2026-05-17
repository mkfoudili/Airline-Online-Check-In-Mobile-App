package com.example.check_in_mobile_app.presentation.checkin

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.check_in_mobile_app.presentation.navigation.CheckInNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CheckInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val bookingRef  = intent.getStringExtra("booking_ref")  ?: ""
        val passengerId = intent.getStringExtra("passenger_id") ?: ""

        setContent {
            CheckInMobileAppTheme {
                CheckInNavGraph(
                    bookingRef  = bookingRef,
                    passengerId = passengerId,
                    onBackFromFirstStep = { finish() },
                    onCheckInComplete = {
                        setResult(RESULT_OK)
                        finish()
                    }
                )
            }
        }
    }
}