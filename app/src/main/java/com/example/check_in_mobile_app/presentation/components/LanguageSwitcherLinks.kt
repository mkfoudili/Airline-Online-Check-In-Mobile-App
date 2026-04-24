package com.example.check_in_mobile_app.presentation.components

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.utils.LanguagePreferences

@Composable
fun LanguageSwitcherLinks(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        LanguageLink(
            text = "English",
            onClick = { changeLanguage(context, "en") }
        )
        Text(
            text = "  •  ",
            fontSize = 12.sp,
            color = MediumGray
        )
        LanguageLink(
            text = "Français",
            onClick = { changeLanguage(context, "fr") }
        )
        Text(
            text = "  •  ",
            fontSize = 12.sp,
            color = MediumGray
        )
        LanguageLink(
            text = "العربية",
            onClick = { changeLanguage(context, "ar") }
        )
    }
}

@Composable
private fun LanguageLink(
    text: String,
    onClick: () -> Unit
) {
    Text(
        text = text,
        fontSize = 12.sp,
        color = NavyBlue,
        modifier = Modifier.clickable { onClick() }
    )
}

private fun changeLanguage(context: android.content.Context, languageCode: String) {
    LanguagePreferences.saveLanguage(context, languageCode)
    val appLocale: LocaleListCompat = LocaleListCompat.forLanguageTags(languageCode)
    AppCompatDelegate.setApplicationLocales(appLocale)
}
