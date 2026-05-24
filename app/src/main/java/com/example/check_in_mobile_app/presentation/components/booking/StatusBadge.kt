package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.CheckedInBg
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.check_in_mobile_app.ui.theme.CheckedInText
import com.example.check_in_mobile_app.ui.theme.LightGray
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.CheckInStatus

import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.R

@Composable
fun StatusBadge(status: CheckInStatus) {
    val (bgColor, textColor, labelRes) = when (status) {
        CheckInStatus.CHECKED_IN    -> Triple(LocalAppColors.current.checkedInBg, LocalAppColors.current.checkedInText, R.string.status_checked_in)
        CheckInStatus.CHECK_IN_OPEN -> Triple(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), MaterialTheme.colorScheme.primary, R.string.status_check_in_open)
        CheckInStatus.CONFIRMED     -> Triple(LocalAppColors.current.chipUnselected, MaterialTheme.colorScheme.primary, R.string.status_confirmed)
        CheckInStatus.PASSED        -> Triple(LocalAppColors.current.chipUnselected, MediumGray,    R.string.status_passed)
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(bgColor)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = stringResource(labelRes),
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

