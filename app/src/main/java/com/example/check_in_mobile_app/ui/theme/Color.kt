package com.example.check_in_mobile_app.ui.theme

import androidx.compose.ui.graphics.Color

// Brand
val NavyBlue    = Color(0xFF25316B)   // primary — buttons, headers, accents
val DarkText    = Color(0xFF2A3343)   // main text
val DarkText111 = Color(0xFF111827)   // very dark text (HomeScreen variant)

// Surfaces & backgrounds
val SurfaceGray   = Color(0xFFF8FAFC) // light card background
val LightGray     = Color(0xFFF3F4F6) // secondary button background, avatar bg
val DividerColor  = Color(0xFFF1F2F4) // horizontal separator
val BorderColor   = Color(0xFFE5E7EB) // input / card border
val BorderLight   = Color(0xFFE2E8F0) // boarding pass card border
val QrBorder      = Color(0xFFBDC1CA) // QR code border

// Secondary text
val SubtleText  = Color(0x992A3343)   // UPPERCASE labels, meta-info
val MediumGray  = Color(0xFF9CA3AF)   // inactive icons, placeholders

// Status colors
val ActiveGreen   = Color(0xFF34D399) // check-in open / active
val WarningAmber  = Color(0xFFF59E0B) // boarding in progress
val ErrorRed      = Color(0xFFEF4444) // boarding closed / error

// Overlays (on navy background)
val BadgeBg       = Color(0x26FFFFFF) // "ACTIVE NOW" badge background
val SubtitleWhite = Color(0xBFFFFFFF) // subtitle on blue card

// Status badge specific (no direct equivalent in design system)
val CheckedInBg   = Color(0xFFDCFCE7) // green pill background
val CheckedInText = Color(0xFF166534) // green pill text