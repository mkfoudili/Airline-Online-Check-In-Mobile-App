package com.example.check_in_mobile_app.presentation.components.flightdetails

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.BorderLight
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.SubtleText
import com.example.check_in_mobile_app.ui.theme.SurfaceGray
import com.example.domain.model.Booking

@Composable
fun FlightInfoCard(booking: Booking, modifier: Modifier = Modifier) {
    val durationMs = booking.flight.arrivalTime - booking.flight.departureTime
    val hours = java.util.concurrent.TimeUnit.MILLISECONDS.toHours(durationMs)
    val minutes = java.util.concurrent.TimeUnit.MILLISECONDS.toMinutes(durationMs) % 60
    val durationText = "${hours}h ${minutes}m".uppercase()

    val sdf = java.text.SimpleDateFormat("dd MMM yyyy", java.util.Locale.getDefault())
    val departureDateText = sdf.format(java.util.Date(booking.flight.departureTime)).uppercase()

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
            .background(Color.White)
    ) {
        // Navy blue header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(NavyBlue)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.plane_up),
                    contentDescription = "Plane icon",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "FLIGHT ${booking.flight.flightNumber}",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // White body
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Origin
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = booking.flight.origin,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue,
                        letterSpacing = (-1).sp
                    )
                    Text(
                        text = booking.flight.originCity,
                        fontSize = 11.sp,
                        color = SubtleText,
                        letterSpacing = 0.5.sp
                    )
                }

                // Middle Dash line
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                            drawLine(
                                color = Color(0xFFCBD5E1),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                            )
                        }
                        Icon(
                            painter = painterResource(id = R.drawable.plane_up),
                            contentDescription = "Plane icon",
                            tint = NavyBlue,
                            modifier = Modifier.padding(horizontal = 8.dp).size(20.dp)
                        )
                        Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                            drawLine(
                                color = Color(0xFFCBD5E1),
                                start = Offset(0f, size.height / 2),
                                end = Offset(size.width, size.height / 2),
                                strokeWidth = 3f,
                                pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 8f))
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = durationText,
                        fontSize = 10.sp,
                        color = SubtleText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }

                // Destination
                Column(
                    modifier = Modifier.weight(1f),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = booking.flight.destination,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue,
                        letterSpacing = (-1).sp,
                        textAlign = TextAlign.End
                    )
                    Text(
                        text = booking.flight.destinationCity,
                        fontSize = 11.sp,
                        color = SubtleText,
                        letterSpacing = 0.5.sp,
                        textAlign = TextAlign.End
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Solid or dashed divider
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
            ) {
                drawLine(
                    color = BorderLight,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 2f
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceGray)
                            .border(1.dp, BorderLight, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "Date",
                            tint = NavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "DATE",
                            fontSize = 10.sp,
                            color = SubtleText,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = departureDateText,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SurfaceGray)
                            .border(1.dp, BorderLight, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.clock),
                            contentDescription = "Time",
                            tint = NavyBlue,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "BOARDING",
                            fontSize = 10.sp,
                            color = SubtleText,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Text(
                            text = booking.flight.boardingTime,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = NavyBlue
                        )
                    }
                }
            }
        }
    }
}
