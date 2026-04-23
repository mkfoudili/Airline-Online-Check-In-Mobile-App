package com.example.check_in_mobile_app.presentation.auth

import android.content.Context
import com.example.check_in_mobile_app.presentation.navigation.LoginNavGraph
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.check_in_mobile_app.presentation.main.MainActivity
import com.example.check_in_mobile_app.ui.theme.CheckInMobileAppTheme
import com.example.check_in_mobile_app.utils.LangUtil
import com.example.check_in_mobile_app.utils.LanguagePreferences

class LoginActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context) {
        val lang = LanguagePreferences.getSavedLanguage(newBase)
        super.attachBaseContext(LangUtil.setLocale(newBase, lang))
    }

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