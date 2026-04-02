package com.example.check_in_mobile_app.presentation.components.booking

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.MediumGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue

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
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = origin,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
            Text(
                text = originCity,
                fontSize = 11.sp,
                color = MediumGray
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
                HorizontalDivider(
                    modifier = Modifier.weight(0.5f),
                    thickness = 1.dp,
                    color = Color(0xFFE2E8F0)
                )
                Icon(
                    imageVector = BookingIcons.Plane,
                    contentDescription = "Flight",
                    tint = NavyBlue,
                    modifier = Modifier.padding(horizontal = 8.dp).size(20.dp)
                )
                HorizontalDivider(
                    modifier = Modifier.weight(0.5f),
                    thickness = 1.dp,
                    color = Color(0xFFE2E8F0)
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = duration,
                fontSize = 11.sp,
                color = MediumGray
            )
        }

        // Right: destination code + city
        Column(horizontalAlignment = Alignment.End) {
            Text(
                text = destination,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )
            Text(
                text = destinationCity,
                fontSize = 11.sp,
                color = MediumGray
            )
        }
    }
}
