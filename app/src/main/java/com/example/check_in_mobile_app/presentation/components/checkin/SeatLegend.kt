package com.example.check_in_mobile_app.presentation.components.checkin

import android.R.color.white
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R

@Composable
fun SeatLegend(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .background(color = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LegendItem(
                seatState = SeatState.AVAILABLE,
                seatType = SeatType.REGULAR,
                label = "Available"
            )
            LegendItem(
                seatState = SeatState.BLOCKED,
                seatType = SeatType.REGULAR,
                label = "Booked"
            )
            LegendItem(
                seatState = SeatState.AVAILABLE,
                seatType = SeatType.PREMIUM,
                label = "Premium"
            )
            LegendItem(
                seatState = SeatState.SELECTED,
                seatType = SeatType.REGULAR,
                label = "Selected"
            )
        }


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
                .background(color = Color(0xFFF9FAFA)),

            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.seat),
                contentDescription = "Front of Aircraft",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "FRONT OF AIRCRAFT",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Gray,
                letterSpacing = 1.5.sp
            )
        }
    }

}

@Composable
private fun LegendItem(
    seatState: SeatState,
    seatType: SeatType,
    label: String
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Seat(
            label = "",
            seatState = seatState,
            seatType = seatType,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = Color.Gray
        )
    }
}