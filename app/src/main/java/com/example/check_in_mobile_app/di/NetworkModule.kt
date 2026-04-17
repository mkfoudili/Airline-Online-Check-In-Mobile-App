package com.example.check_in_mobile_app.di

import android.content.Context

object NetworkModule {

    fun provideNetworkMonitor(context: Context): NetworkMonitor {
        return NetworkMonitor(context)
    }
}