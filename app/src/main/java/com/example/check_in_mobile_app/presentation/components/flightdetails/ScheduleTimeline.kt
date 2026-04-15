package com.example.check_in_mobile_app.presentation.components.flightdetails

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
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Booking

@Composable
fun ScheduleTimeline(booking: Booking, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        TimelineItem(
            time = booking.flight.checkInOpensTime,
            title = "Check-In Opens",
            iconRes = R.drawable.suitcase
        )
        TimelineSpacing()
        TimelineItem(
            time = booking.flight.boardingTime,
            title = "Boarding Starts",
            subtitle = "Gate ${booking.gate}",
            iconRes = R.drawable.clock
        )
        TimelineSpacing()
        
        val sdfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
        val depTimeStr = sdfTime.format(java.util.Date(booking.flight.departureTime))
        
        TimelineItem(
            time = depTimeStr,
            title = "Departure",
            subtitle = "${booking.flight.originCity} (${booking.flight.origin})",
            iconRes = R.drawable.plane_up
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
                .background(Color.White)
                .border(1.dp, BorderLight, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                tint = NavyBlue,
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
                color = DarkText
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = DarkText
            )
            if (subtitle != null) {
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = MediumGray
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
                .background(BorderLight)
        )
    }
}
