package com.example.check_in_mobile_app.presentation.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun rememberScreenWidth(): Dp {
    return LocalConfiguration.current.screenWidthDp.dp
}