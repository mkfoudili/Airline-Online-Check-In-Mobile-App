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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus

@Composable
fun BookingCard(
    booking: Booking,
    onCheckInClick: (String) -> Unit = {},
    onBoarding: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .border(1.dp, Color(0xFFF1F5F9), RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
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
                    .background(Color(0xFFF4F5F8)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plane2),
                    contentDescription = "Flight",
                    tint = NavyBlue,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = booking.flight.flightNumber,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue,
                modifier = Modifier.weight(1f)
            )

            StatusBadge(status = booking.status)
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider(color = Color(0xFFE2E8F0), thickness = 1.dp)
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
                    tint = NavyBlue,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val sdfDate = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
                val departureDate = sdfDate.format(java.util.Date(booking.flight.departureTime))
                
                Text(
                    text = departureDate,
                    fontSize = 13.sp,
                    color = NavyBlue,
                    fontWeight = FontWeight.Medium
                )
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    painter = painterResource(id = R.drawable.clock),
                    contentDescription = "Time",
                    tint = NavyBlue,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val sdfTime = java.text.SimpleDateFormat("HH:mm", java.util.Locale.getDefault())
                val departureTime = sdfTime.format(java.util.Date(booking.flight.departureTime))
                
                Text(
                    text = departureTime,
                    fontSize = 13.sp,
                    color = NavyBlue,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // ── Row 4: bottom action (depends on status) ─────────────────
        when (booking.status) {
            CheckInStatus.CHECKED_IN -> {
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { onBoarding() },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, NavyBlue),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = NavyBlue,

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
                    onClick = { onCheckInClick(booking.bookingId) },
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
                Spacer(modifier = Modifier.height(12.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFE2E8F0))
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.check_in_opens_in, "4h"),
                        fontSize = 13.sp,
                        color = MediumGray,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            CheckInStatus.PASSED -> { /* flight passed, no action needed */ }
        }
        }
    }
