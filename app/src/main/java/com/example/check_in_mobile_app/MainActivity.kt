package com.example.check_in_mobile_app

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.check_in_mobile_app.presentation.navigation.AppNavGraph
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckInMobileAppTheme {
                AppNavGraph()
            }
        }
    }
}