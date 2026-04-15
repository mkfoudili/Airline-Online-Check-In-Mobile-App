package com.example.check_in_mobile_app.presentation.components.checkin.checkingreviewdetails

import androidx.compose.foundation.background
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Passenger

@Composable
fun PassengerAvatarRow(passenger: Passenger) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        // Circular avatar placeholder
        Box(
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .background(Color(0xFFE2E8F0)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.user),
                contentDescription = null,
                tint = Color(0xFF64748B),
                modifier = Modifier.size(32.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            // Full name
            Text(
                text = "${passenger.firstName} ${passenger.lastName}",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue
            )

            Spacer(modifier = Modifier.height(6.dp))


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.circle_check),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(25.dp)
                )
                Text(
                    text = "PASSPORT SCANNED",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF10B981),
                    letterSpacing = 0.6.sp
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PassengerAvatarRowPreview() {
    val previewPassenger = com.example.data.repository.CheckInRepositoryImpl()
        .getPassengerForReview()
    Box(modifier = Modifier.padding(16.dp)) {
        PassengerAvatarRow(passenger = previewPassenger)
    }
}
