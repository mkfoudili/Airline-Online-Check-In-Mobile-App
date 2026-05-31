package com.example.check_in_mobile_app.presentation.components.boarding

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.LocalAppColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoardingTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.boarding_pass),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.chevron_left),
                    contentDescription = stringResource(R.string.back),
                    tint = LocalAppColors.current.textPrimary
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant, thickness = 1.dp)
}

@Composable
fun BoardingFooter(boardingTime: String, gate: String) {
    val minutesUntilBoarding = remember(boardingTime) {
        try {
            val sdf = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
            val boarding = sdf.parse(boardingTime) ?: return@remember null
            val now = java.util.Calendar.getInstance()
            val cal = java.util.Calendar.getInstance().apply {
                time = boarding
                set(java.util.Calendar.YEAR, now.get(java.util.Calendar.YEAR))
                set(java.util.Calendar.MONTH, now.get(java.util.Calendar.MONTH))
                set(java.util.Calendar.DAY_OF_MONTH, now.get(java.util.Calendar.DAY_OF_MONTH))
            }
            val diff = (cal.timeInMillis - now.timeInMillis) / (1000 * 60)
            if (diff >= 0) diff else null
        } catch (e: Exception) {
            null
        }
    }

    val boardingLabel = when {
        minutesUntilBoarding == null -> "Boarding at $boardingTime"
        minutesUntilBoarding == 0L   -> "Boarding now"
        else                         -> "Boarding starts in ${minutesUntilBoarding}m"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.clock),
            contentDescription = null,
            modifier = Modifier.padding(0.dp, 5.dp),
            tint = LocalAppColors.current.textPrimary
        )
        Column {
            Text(
                text = boardingLabel,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = LocalAppColors.current.textPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = "Head to Gate $gate. Security wait time is currently approximately 15 minutes.",
                fontSize = 11.sp,
                color = LocalAppColors.current.textSubtle,
                lineHeight = 16.sp
            )
        }
    }
}