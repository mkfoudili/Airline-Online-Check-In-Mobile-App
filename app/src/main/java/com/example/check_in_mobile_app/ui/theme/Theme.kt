package com.example.check_in_mobile_app.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import java.util.Locale

data class AppColors(
    val surface: Color,
    val surfaceVariant: Color,
    val surfaceElevated: Color,
    val background: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val textSubtle: Color,
    val textAccent: Color,
    val border: Color,
    val borderLight: Color,
    val divider: Color,
    val iconBackground: Color,
    val chipUnselected: Color,
    val inputBackground: Color,
    val checkedInBg: Color,
    val checkedInText: Color,
    val navBackground: Color,
)

val LightAppColors = AppColors(
    surface         = Color.White,
    surfaceVariant  = SurfaceGray,
    surfaceElevated = Color.White,
    background      = Color.White,
    textPrimary     = DarkText,
    textSecondary   = Slate500,
    textSubtle      = SubtleText,
    textAccent      = NavyBlue,
    border          = BorderColor,
    borderLight     = BorderLight,
    divider         = DividerColor,
    iconBackground  = Color(0xFFF4F5F8),
    chipUnselected  = Color(0xFFF1F5F9),
    inputBackground = Color.White,
    checkedInBg     = CheckedInBg,
    checkedInText   = CheckedInText,
    navBackground   = Color.White,
)

val DarkAppColors = AppColors(
    surface         = DarkSurface,
    surfaceVariant  = DarkSurfaceGray,
    surfaceElevated = DarkSurfaceVariant,
    background      = DarkBackground,
    textPrimary     = Color(0xFFECEFF4),   // blanc cassé très lisible
    textSecondary   = Slate500Dark,         // gris clair
    textSubtle      = SubtleTextDark,       // gris medium
    textAccent      = NavyBlueDark,         // bleu clair
    border          = DarkBorder,
    borderLight     = DarkBorderLight,
    divider         = DarkDivider,
    iconBackground  = Color(0xFF252A3D),
    chipUnselected  = Color(0xFF252A3D),
    inputBackground = DarkSurfaceVariant,
    checkedInBg     = CheckedInBgDark,
    checkedInText   = CheckedInTextDark,
    navBackground   = DarkSurface,
)

val LocalAppColors = compositionLocalOf { LightAppColors }

val appColors: AppColors
    @Composable
    get() = LocalAppColors.current

private val LightColorScheme = lightColorScheme(
    primary          = NavyBlue,
    onPrimary        = Color.White,
    secondary        = ActiveGreen,
    onSecondary      = Color.White,
    error            = ErrorRed,
    onError          = Color.White,
    background       = Color.White,
    onBackground     = DarkText,
    surface          = Color.White,
    onSurface        = DarkText,
    surfaceVariant   = SurfaceGray,
    onSurfaceVariant = Slate500,
    outline          = BorderColor
)

private val DarkColorScheme = darkColorScheme(
    primary          = NavyBlueDark,
    onPrimary        = Color.White,
    secondary        = ActiveGreen,
    onSecondary      = Color.White,
    error            = ErrorRed,
    onError          = Color.White,
    background       = DarkBackground,
    onBackground     = Color(0xFFECEFF4),
    surface          = DarkSurface,
    onSurface        = Color(0xFFECEFF4),
    surfaceVariant   = DarkSurfaceGray,
    onSurfaceVariant = Slate500Dark,
    outline          = DarkBorder,
    outlineVariant   = DarkBorderLight,
    inverseSurface   = Color(0xFFECEFF4),
    inverseOnSurface = DarkBackground,
)

@Composable
fun CheckInMobileAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else      -> LightColorScheme
    }

    val appColors = if (darkTheme) DarkAppColors else LightAppColors

    val layoutDirection = if (Locale.getDefault().language == "ar") {
        LayoutDirection.Rtl
    } else {
        LayoutDirection.Ltr
    }

    CompositionLocalProvider(
        LocalLayoutDirection provides layoutDirection,
        LocalAppColors provides appColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography  = Typography,
            content     = content
        )
    }
}