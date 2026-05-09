package com.example.check_in_mobile_app.presentation.components.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.main.home.HomeUiState
import com.example.check_in_mobile_app.ui.theme.*

@Composable
fun ActiveFlightCard(
    uiState: HomeUiState,
    onCheckInClick: () -> Unit
) {
    val isRtl = LocalLayoutDirection.current == LayoutDirection.Rtl
    val planeScaleX = if (isRtl) -1f else 1f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(NavyBlue)
            .padding(22.dp)
    ) {
        // Avion décoratif
        Image(
            painter = painterResource(id = R.drawable.plane),
            contentScale = ContentScale.Crop,
            contentDescription = null,
            modifier = Modifier
                .width(200.dp)
                .height(200.dp)
                .offset(137.dp, 25.dp)
                .scale(scaleX = planeScaleX, scaleY = 1f)
        )

        Column {
            // Badge
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(BadgeBg)
                    .padding(horizontal = 12.dp, vertical = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .clip(CircleShape)
                        .background(if (uiState.activeFlight != null) ActiveGreen else CoolGray)
                )
                Text(
                    text = stringResource(R.string.active_now_badge),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    letterSpacing = 0.8.sp,
                    maxLines = 1
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.active_ready_to_fly),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                letterSpacing = (-0.3).sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(6.dp))

            when {
                uiState.isActiveFlightLoading -> {
                    CircularProgressIndicator(
                        color = SubtitleWhite,
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                }
                uiState.activeFlight != null -> {
                    Text(
                        text = if (uiState.isCheckInActive)
                            stringResource(R.string.active_checkin_open, uiState.activeFlightDestination)
                        else
                            stringResource(R.string.active_flight_upcoming, uiState.activeFlightDestination),
                        fontSize = 13.sp,
                        color = SubtitleWhite,
                        lineHeight = 19.sp,
                        modifier = Modifier.widthIn(max = 200.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                else -> {
                    Text(
                        text = stringResource(R.string.active_book_flight),
                        fontSize = 13.sp,
                        color = SubtitleWhite,
                        lineHeight = 19.sp,
                        modifier = Modifier.widthIn(max = 200.dp),
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            if (uiState.isCheckInActive) {
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onCheckInClick,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = NavyBlue
                    ),
                    contentPadding = PaddingValues(horizontal = 22.dp, vertical = 11.dp),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
                ) {
                    Text(
                        text = stringResource(R.string.active_check_in_now),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}