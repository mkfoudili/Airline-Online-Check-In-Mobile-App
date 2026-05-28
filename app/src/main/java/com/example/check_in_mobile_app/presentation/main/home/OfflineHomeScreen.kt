package com.example.check_in_mobile_app.presentation.main.home

import com.example.check_in_mobile_app.ui.theme.LocalAppColors

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.check_in_mobile_app.ui.theme.*
import com.example.domain.model.Flight
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OfflineHomeScreen(
    onNavigateToFlightDetails: (flightId: String) -> Unit = {},
    screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp,
    uiState: HomeUiState
) {
    // Header
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(MaterialTheme.colorScheme.surface),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.offline_dashboard_title),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = LocalAppColors.current.textAccent,
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
                tint = ErrorRed,
                modifier = Modifier.size(10.dp)
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

    // Section titre
    Text(
        text = stringResource(R.string.offline_upcoming_flight),
        fontSize = 16.sp,
        letterSpacing = 0.7.sp,
        fontWeight = FontWeight.Bold,
        color = LocalAppColors.current.textSubtle,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )

    Spacer(modifier = Modifier.height(16.dp))

    val now = System.currentTimeMillis()
    val threeDaysMs = 5 * 24 * 60 * 60 * 1000L

    val upcomingFlights = uiState.cachedFlights.filter { it.departureTime >= now }
    val pastFlights = uiState.cachedFlights.filter {
        it.departureTime in (now - threeDaysMs)..(now - 1)
    }

    if (uiState.cachedFlights.isEmpty()) {
        // Aucun vol en cache
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(LocalAppColors.current.iconBackground)
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.offline_no_cached_flight),
                fontSize = 14.sp,
                color = LocalAppColors.current.textSubtle,
                fontWeight = FontWeight.Medium
            )
        }
    } else {
        // --- Vols à venir ---
        if (upcomingFlights.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                upcomingFlights.forEach { flight ->
                    CachedFlightCard(
                        flight = flight,
                        onViewDetail = { onNavigateToFlightDetails(flight.flightId) }
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(LocalAppColors.current.iconBackground)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.offline_no_upcoming_flight),
                    fontSize = 14.sp,
                    color = LocalAppColors.current.textSubtle,
                    fontWeight = FontWeight.Medium
                )
            }
        }

        // --- Vols passés ---
        if (pastFlights.isNotEmpty()) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.offline_past_flights),
                fontSize = 16.sp,
                letterSpacing = 0.7.sp,
                fontWeight = FontWeight.Bold,
                color = LocalAppColors.current.textSubtle,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(12.dp))
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                pastFlights.forEach { flight ->
                    CachedFlightCard(
                        flight = flight,
                        onViewDetail = { onNavigateToFlightDetails(flight.flightId) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Note d'information
        val dashedBorderColor = LocalAppColors.current.textAccent
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .drawBehind {
                    drawRoundRect(
                        color = dashedBorderColor,
                        size = size,
                        cornerRadius = CornerRadius(12.dp.toPx()),
                        style = Stroke(
                            width = 1.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        )
                    )
                }
                .padding(horizontal = 16.dp, vertical = 10.dp),
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
                    tint = LocalAppColors.current.textSubtle,
                    modifier = Modifier
                        .size(18.dp)
                        .offset(0.dp, 5.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = stringResource(R.string.offline_data_saved_note),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal,
                    color = LocalAppColors.current.textSubtle,
                    lineHeight = 19.sp
                )
            }
        }
    }
}

@Composable
private fun CachedFlightCard(
    flight: Flight,
    onViewDetail: () -> Unit
) {
    val sdf = SimpleDateFormat("dd MMM yyyy  HH:mm", Locale.getDefault())
    val departureFormatted = if (flight.departureTime > 0L)
        sdf.format(Date(flight.departureTime))
    else "--"

    Card(
        modifier = Modifier.fillMaxWidth().border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Route
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = flight.origin,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textAccent
                    )
                    Text(
                        text = flight.originCity,
                        fontSize = 12.sp,
                        color = LocalAppColors.current.textSubtle
                    )
                }
                Icon(
                    painter = painterResource(id = R.drawable.plane),
                    contentDescription = null,
                    tint = LocalAppColors.current.textAccent,
                    modifier = Modifier.size(24.dp)
                )
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = flight.destination,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = LocalAppColors.current.textAccent
                    )
                    Text(
                        text = flight.destinationCity,
                        fontSize = 12.sp,
                        color = LocalAppColors.current.textSubtle
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Spacer(modifier = Modifier.height(12.dp))

            // Infos vol
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FlightInfoItem(label = "Flight", value = flight.flightNumber)
                FlightInfoItem(label = "Terminal", value = flight.terminal.ifBlank { "--" })
                FlightInfoItem(label = "Gate", value = flight.gate.ifBlank { "--" })
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = departureFormatted,
                fontSize = 12.sp,
                color = LocalAppColors.current.textSubtle,
                modifier = Modifier.align(Alignment.End)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bouton voir détail
            OutlinedButton(
                onClick = onViewDetail,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = LocalAppColors.current.textAccent),
                border = androidx.compose.foundation.BorderStroke(1.dp, LocalAppColors.current.textAccent)
            ) {
                Text(
                    text = stringResource(R.string.offline_view_flight_details),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun FlightInfoItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, fontSize = 11.sp, color = LocalAppColors.current.textSubtle)
        Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = LocalAppColors.current.textAccent)
    }
}