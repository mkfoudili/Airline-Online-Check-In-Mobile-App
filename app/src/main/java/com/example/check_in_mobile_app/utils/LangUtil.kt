package com.example.check_in_mobile_app.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import java.util.Locale

object LangUtil {

    fun setLocale(context: Context, languageCode: String): Context {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)

        val resources = context.resources
        val configuration = Configuration(resources.configuration)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
        } else {
            @Suppress("DEPRECATION")
            configuration.locale = locale
        }

        // Also update application resources to ensure global string consistency
        @Suppress("DEPRECATION")
        context.applicationContext.resources.updateConfiguration(configuration, resources.displayMetrics)

        // Force update the current context's resources as well (fallback for some Android versions)
        @Suppress("DEPRECATION")
        resources.updateConfiguration(configuration, resources.displayMetrics)

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            context.createConfigurationContext(configuration)
        } else {
            context
        }
    }
}
