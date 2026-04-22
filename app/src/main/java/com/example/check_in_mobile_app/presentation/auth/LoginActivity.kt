package com.example.check_in_mobile_app.presentation.auth

import com.example.check_in_mobile_app.presentation.navigation.LoginNavGraph
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CheckInMobileAppTheme {
                LoginNavGraph(
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