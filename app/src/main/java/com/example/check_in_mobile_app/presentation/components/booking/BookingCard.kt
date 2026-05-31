package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.background
import androidx.compose.ui.res.stringResource
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.example.check_in_mobile_app.R
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus

@Composable
fun BookingCard(
    booking: Booking,
    onCheckInClick: (String) -> Unit = {},
    onBoarding: (String) -> Unit = {},
    showPassengerName: Boolean = true
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(LocalAppColors.current.surface)
            .padding(16.dp)
    ) {
        // ── Row 1: flight icon + number + status badge ──────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Small plane icon in a rounded box
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(LocalAppColors.current.iconBackground),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plane2),
                    contentDescription = "Flight",
                    tint = LocalAppColors.current.textAccent,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = booking.flight.flightNumber,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = LocalAppColors.current.textAccent
                )
                val passenger = booking.passengers
                    .firstOrNull { it.passengerId == booking.checkinPassengerId }
                    ?: booking.passengers.firstOrNull()
                if (showPassengerName && passenger != null) {
                    Text(
                        text = "${passenger.firstName} ${passenger.lastName}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Normal,
                        color = LocalAppColors.current.textSecondary
                    )
                }
            }

            StatusBadge(status = booking.status)
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = LocalAppColors.current.divider, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))


        // ── Row 2: Origin ── ✈ duration ── Destination ──────────────
        val durationDiff = booking.flight.arrivalTime - booking.flight.departureTime
        val durationFormatted = if (durationDiff > 0) {
            val hours = durationDiff / (1000 * 60 * 60)
            val mins = (durationDiff / (1000 * 60)) % 60
            "${hours}${stringResource(R.string.unit_hours)} ${mins}${stringResource(R.string.unit_minutes)}"
        } else ""

        FlightRouteRow(
            origin = booking.flight.origin,
            originCity = booking.flight.originCity,
            destination = booking.flight.destination,
            destinationCity = booking.flight.destinationCity,
            duration = durationFormatted
        )

        Spacer(modifier = Modifier.height(16.dp))



        Spacer(modifier = Modifier.height(12.dp))

        // ── Row 3: date + time ───────────────────────────────────────
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.calendar),
                    contentDescription = "Date",
                    tint = LocalAppColors.current.textAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val sdfDate = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                val departureDate = sdfDate.format(java.util.Date(booking.flight.departureTime))

                Text(
                    text = departureDate,
                    fontSize = 13.sp,
                    color = LocalAppColors.current.textAccent,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Time",
                    tint = LocalAppColors.current.textAccent,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val sdfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                val departureTime = sdfTime.format(java.util.Date(booking.flight.departureTime))

                Text(
                    text = departureTime,
                    fontSize = 13.sp,
                    color = LocalAppColors.current.textAccent,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ── Row 4: bottom action (depends on status) ─────────────────
        when (booking.status) {
            CheckInStatus.CHECKED_IN -> {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onBoarding(booking.checkinPassengerId ?: "") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, LocalAppColors.current.textAccent),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = LocalAppColors.current.textAccent,

                        )
                ) {
                    Text(
                        text = stringResource(R.string.boarding_pass),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            CheckInStatus.CHECK_IN_OPEN -> {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onCheckInClick(booking.bookingRef) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.check_in),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            CheckInStatus.CONFIRMED -> {
                val now = System.currentTimeMillis()
                val departureTime = booking.flight.departureTime
                val checkInOpenTime = departureTime - (24L * 60 * 60 * 1000)
                val diffMs = checkInOpenTime - now
                val diffMinutes = (diffMs / (1000 * 60)).coerceAtLeast(1)
                val timeString = if (diffMinutes >= 60) {
                    val hours = diffMinutes / 60
                    if (hours >= 24) {
                        val days = hours / 24
                        val remainingHours = hours % 24
                        if (remainingHours > 0) "${days}d ${remainingHours}h" else "${days}d"
                    } else {
                        "${hours}h"
                    }
                } else {
                    "${diffMinutes}m"
                }

                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(LocalAppColors.current.chipUnselected)
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.check_in_opens_in, timeString),
                        fontSize = 13.sp,
                        color = LocalAppColors.current.textSecondary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            CheckInStatus.PASSED -> { /* flight passed, no action needed */ }
        }
    }
}