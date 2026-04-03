package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Slate500

@Composable
fun FlightRouteRow(
    origin: String,
    originCity: String,
    destination: String,
    destinationCity: String,
    duration: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left: origin code + city
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = origin,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
            Text(
                text = originCity.uppercase(),
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = Slate500
            )
        }

        // Centre: line + ✈ + duration
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                    drawLine(
                        color = Color(0xFFCBD5E1),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 3f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 6f))
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.plane2),
                    contentDescription = "Flight",
                    tint = NavyBlue,
                    modifier = Modifier.padding(horizontal = 8.dp).size(20.dp)
                )
                Canvas(modifier = Modifier.weight(1f).height(8.dp)) {
                    drawLine(
                        color = Color(0xFFCBD5E1),
                        start = Offset(0f, size.height / 2),
                        end = Offset(size.width, size.height / 2),
                        strokeWidth = 3f,
                        pathEffect = androidx.compose.ui.graphics.PathEffect.dashPathEffect(floatArrayOf(10f, 6f))
                    )
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = duration,
                fontSize = 11.sp,
                color = MediumGray
            )
        }

        // Right: destination code + city
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = destination,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
            Text(
                text = destinationCity.uppercase(),
                fontSize = 11.sp,
                letterSpacing = 1.sp,
                color = Slate500
            )
        }
    }
}
