package com.example.check_in_mobile_app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CheckInApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}