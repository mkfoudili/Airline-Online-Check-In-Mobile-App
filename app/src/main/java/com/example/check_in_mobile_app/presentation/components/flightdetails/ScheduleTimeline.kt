package com.example.check_in_mobile_app.presentation.components.flightdetails

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.domain.model.Booking

@Composable
fun ScheduleTimeline(booking: Booking, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        TimelineItem(
            time = booking.flight.checkInOpensTime,
            title = stringResource(R.string.checkin_opens_label),
            iconRes = R.drawable.suitcase
        )
        TimelineSpacing()
        TimelineItem(
            time = booking.flight.boardingTime,
            title = stringResource(R.string.boarding_starts_label),
            subtitle = "${stringResource(R.string.gate_label)} ${booking.flight.gate} • ${booking.flight.terminal}",
            iconRes = R.drawable.clock
        )
        TimelineSpacing()

        val sdfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        val depTimeStr = sdfTime.format(java.util.Date(booking.flight.departureTime))

        TimelineItem(
            time = depTimeStr,
            title = stringResource(R.string.departure_label),
            subtitle = "${booking.flight.originCity} (${booking.flight.origin})",
            iconRes = R.drawable.plane_up
        )
        TimelineSpacing()

        val arrTimeStr = if (booking.flight.arrivalTime > 0L)
            sdfTime.format(java.util.Date(booking.flight.arrivalTime))
        else "--"

        TimelineItem(
            time = arrTimeStr,
            title = stringResource(R.string.arrival_label),
            subtitle = "${booking.flight.destinationCity} (${booking.flight.destination})",
            iconRes = R.drawable.plane2
        )
    }
}

@Composable
fun TimelineItem(
    time: String,
    title: String,
    subtitle: String? = null,
    iconRes: Int
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Icon container
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, MaterialTheme.colorScheme.outlineVariant, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = LocalAppColors.current.textAccent,
                modifier = Modifier.size(16.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Content
        Column(modifier = Modifier.padding(top = 2.dp)) {
            Text(
                text = time,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textPrimary
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = LocalAppColors.current.textPrimary
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = LocalAppColors.current.textSecondary
                )
            }
        }
    }
}

@Composable
fun TimelineSpacing() {
    Row {
        Spacer(modifier = Modifier.width(19.dp)) // Center of the 40dp icon (20dp minus 1dp width)
        Box(
            modifier = Modifier
                .width(2.dp)
                .height(30.dp)
                .background(MaterialTheme.colorScheme.outlineVariant)
        )
    }
}