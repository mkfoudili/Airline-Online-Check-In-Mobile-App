package com.example.check_in_mobile_app.presentation.components.checkin


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun Seat(
    label: String,
    seatState: SeatState = SeatState.AVAILABLE,
    seatType: SeatType = SeatType.REGULAR,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    seatSize: androidx.compose.ui.unit.Dp = 48.dp
) {
    val backgroundColor = when (seatState) {
        SeatState.AVAILABLE -> Color.White
        SeatState.SELECTED  -> Color(0xFF34A853)
        SeatState.BLOCKED   -> NavyBlue
    }

    val iconTint = when (seatState) {
        SeatState.SELECTED -> Color.White
        else               -> NavyBlue
    }

    val iconSize = seatSize * 0.3f

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(seatSize)
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor)
            .border(
                width = 1.dp,
                color = Color(0xFFE0E0E0),
                shape = RoundedCornerShape(10.dp)
            )
            .clickable(enabled = seatState != SeatState.BLOCKED) { onClick() }
    ) {
        when {
            seatType == SeatType.PREMIUM -> {
                Icon(
                    painter = painterResource(id = com.example.check_in_mobile_app.R.drawable.zap),
                    contentDescription = "Premium Seat",
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
            }
            seatState == SeatState.BLOCKED -> {
                Icon(
                    painter = painterResource(id = com.example.check_in_mobile_app.R.drawable.users),
                    contentDescription = "Blocked",
                    tint = Color.LightGray,
                    modifier = Modifier.size(iconSize)
                )
            }
            seatState == SeatState.SELECTED -> {
                Icon(
                    painter = painterResource(id = com.example.check_in_mobile_app.R.drawable.check2),
                    contentDescription = "Selected",
                    tint = Color.White,
                    modifier = Modifier.size(iconSize)
                )
            }
            else -> {
                Text(
                    text = label,
                    fontSize = (seatSize.value * 0.3f).sp,
                    fontWeight = FontWeight.SemiBold,
                    color = NavyBlue
                )
            }
        }
    }
}