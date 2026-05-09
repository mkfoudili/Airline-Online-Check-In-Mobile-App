package com.example.check_in_mobile_app.presentation.main.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.OfflineBanner
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.ui.theme.*
import java.util.concurrent.TimeUnit

@Composable
fun OfflineHomeScreen(
    onNavigateToBoardingScreen: () -> Unit = {},
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    uiState: HomeUiState
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.offline_dashboard_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = NavyBlue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.wrapContentWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.wifi_off2),
                contentDescription = null,
                tint = ErrorRed
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = stringResource(R.string.offline_badge),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = ErrorRed,
                maxLines = 1
            )
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Box(modifier = Modifier.requiredWidth(screenWidth)) {
        OfflineBanner(
            iconId = R.drawable.cloud_off,
            iconDescription = "cloud-off",
            title = stringResource(R.string.offline_viewing_cached),
            description = stringResource(R.string.offline_features_require_internet)
        )
    }

    Spacer(modifier = Modifier.height(24.dp))

    // Affiche la date de sync si on a un vol en cache
    val cachedBooking = uiState.activeFlight
    val syncedAgo = cachedBooking?.let {
        val now = System.currentTimeMillis()
        // On approxime depuis le lastSyncedAt de l'entity, ici on utilise le timestamp actuel comme proxy
        val diff = now - (it.flight.departureTime - 86_400_000L) // heuristique simple
        val mins = TimeUnit.MILLISECONDS.toMinutes(kotlin.math.abs(diff)).coerceAtMost(59)
        "$mins min"
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.offline_upcoming_flight),
            fontSize = 16.sp,
            letterSpacing = 0.7.sp,
            fontWeight = FontWeight.Bold,
            color = CoolGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Row(
            modifier = Modifier.width(150.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.sync),
                contentDescription = null,
                tint = CoolGray
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = syncedAgo?.let {
                    stringResource(R.string.offline_synced_ago, it)
                } ?: stringResource(R.string.offline_not_synced),
                fontSize = 9.sp,
                color = CoolGray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }

    Spacer(modifier = Modifier.height(24.dp))

    if (cachedBooking != null) {
        FlightInfoCard(booking = cachedBooking)

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.offline_available_actions),
            fontSize = 16.sp,
            letterSpacing = 0.7.sp,
            fontWeight = FontWeight.Bold,
            color = CoolGray
        )

        Spacer(modifier = Modifier.height(16.dp))

        BoardingPassButton(onNavigateToBoardingScreen)

        Spacer(modifier = Modifier.height(50.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .drawBehind {
                    drawRoundRect(
                        color = NavyBlue,
                        size = size,
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    )
                }
                .padding(16.dp, vertical = 10.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.Top
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.info),
                    contentDescription = null,
                    tint = CoolGray,
                    modifier = Modifier.size(18.dp).offset(0.dp, 5.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.offline_data_saved_note),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = CoolGray,
                    lineHeight = 19.sp
                )
            }
        }
    } else {
        // Aucun vol en cache
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LightGray)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.offline_no_cached_flight),
                fontSize = 14.sp,
                color = CoolGray,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun BoardingPassButton(onNavigateToBoardingScreen: () -> Unit) {
    Button(
        onClick = onNavigateToBoardingScreen,
        modifier = Modifier.fillMaxWidth().height(82.dp),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, NavyBlue),
        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = NavyBlue)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.width(48.dp).height(48.dp)
                    .clip(RoundedCornerShape(6.dp)).background(NavyBlue),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.qr_code),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.offset((10).dp, 0.dp).weight(1f),
            ) {
                Text(
                    text = stringResource(R.string.offline_view_boarding_pass),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = NavyBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = stringResource(R.string.offline_accessible_without_connection),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal,
                    color = CoolGray,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Icon(
                painter = painterResource(id = R.drawable.chevron_right),
                contentDescription = null,
                tint = NavyBlue
            )
        }
    }
}