package com.example.check_in_mobile_app.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.OfflineBanner
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.ui.theme.ErrorRed
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.CoolGray
import com.example.domain.model.Booking

@Composable
fun OfflineHomeScreen(
    onNavigateToBoardingScreen: () -> Unit = {},
    screenWidth : Dp = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    },
    booking: Booking
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(
                Color.White
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Offline Dashboard",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NavyBlue
        )
        Row(
            modifier = Modifier
                .width(70.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wifi_off2),
                contentDescription = "Wifi off Icon",
                tint = ErrorRed
            )
            Text(
                text = "OFFLINE",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ErrorRed
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Box(
        modifier = Modifier
            .requiredWidth(screenWidth)
    ) {
        OfflineBanner(
            iconId = R.drawable.cloud_off,
            iconDescription = "Cloud Icon",
            title = "Viewing Cached Data",
            description = "Some features require an internet connection."
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color.White
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Upcoming Flight",
            fontSize = 16.sp,
            letterSpacing = 0.7.sp,
            fontWeight = FontWeight.Bold,
            color = CoolGray
        )
        Row(
            modifier = Modifier
                .width(120.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sync),
                contentDescription = "Wifi off Icon",
                tint = CoolGray
            )
            Text(
                text = "Synced 42 mins ago",
                fontSize = 9.sp,
                color = CoolGray
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    FlightInfoCard(
        booking = booking
    )

    Spacer(modifier = Modifier.height(30.dp))

    Text(
        text = "AVAILABLE ACTIONS",
        fontSize = 16.sp,
        letterSpacing = 0.7.sp,
        fontWeight = FontWeight.Bold,
        color = CoolGray
    )

    Spacer(modifier = Modifier.height(16.dp))

    BoardingPassButton (
        onNavigateToBoardingScreen
    )

    Spacer(modifier = Modifier.height(50.dp))

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(75.dp)
            .drawBehind {
                drawRoundRect(
                    color = NavyBlue,
                    size = size,
                    cornerRadius = CornerRadius(12.dp.toPx()),
                    style = Stroke(
                        width = 1.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            intervals = floatArrayOf(10f, 10f),
                            phase = 0f
                        )
                    )
                )
            }
            .padding(16.dp, vertical = 0.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                painter = painterResource(id = R.drawable.info),
                contentDescription = "Info Icon",
                tint = CoolGray,
                modifier = Modifier.size(18.dp)
                    .offset(0.dp, 5.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Your flight data was saved automatically for offline access. To update information, reconnect to Wi-Fi or Cellular data.",
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = CoolGray,
                lineHeight = 19.sp,
            )
        }
    }
}

@Composable
private fun BoardingPassButton(onNavigateToBoardingScreen: () -> Unit) {
    Button(
        onClick = {
            onNavigateToBoardingScreen()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(82.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, NavyBlue),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = NavyBlue
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(NavyBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.qr_code),
                    contentDescription = "QR Code Icon",
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .offset((-10).dp, 0.dp)
            ) {
                Text(
                    text = "View Boarding Pass",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyBlue
                )
                Text(
                    text = "Accessible without connection",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = CoolGray
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = "Arrow Right Icon",
                tint = NavyBlue
            )
        }
    }
}