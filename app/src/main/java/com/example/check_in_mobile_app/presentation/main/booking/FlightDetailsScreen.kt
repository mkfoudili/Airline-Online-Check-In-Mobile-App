package com.example.check_in_mobile_app.presentation.main.booking

import androidx.compose.material3.MaterialTheme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.presentation.components.flightdetails.ScheduleTimeline
import com.example.check_in_mobile_app.ui.theme.*
import com.example.check_in_mobile_app.ui.theme.LocalAppColors
import com.example.domain.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    onBack: () -> Unit = {},
    onStartCheckIn: (passengerId: String, bookingId: String) -> Unit = { _, _ -> },
    viewModel: FlightDetailsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is FlightDetailsUiState.Loading -> {
            // Fond blanc + TopAppBar visible pour éviter le flash gris pendant la navigation
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_left),
                                    contentDescription = null,
                                    tint = LocalAppColors.current.textPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = NavyBlue)
                }
            }
        }
        is FlightDetailsUiState.Error -> {
            Scaffold(
                containerColor = MaterialTheme.colorScheme.background,
                topBar = {
                    TopAppBar(
                        title = {},
                        navigationIcon = {
                            IconButton(onClick = onBack) {
                                Icon(
                                    painter = painterResource(id = R.drawable.chevron_left),
                                    contentDescription = null,
                                    tint = LocalAppColors.current.textPrimary
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
                    )
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = state.message, color = Color.Red, modifier = Modifier.padding(20.dp))
                }
            }
        }
        is FlightDetailsUiState.Success -> {
            FlightDetailsScreenContent(
                booking    = state.booking,
                isOnline   = state.isOnline,
                onBack     = onBack,
                onStartCheckIn = onStartCheckIn
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreenContent(
    booking: Booking,
    isOnline: Boolean = true,
    onBack: () -> Unit,
    onStartCheckIn: (passengerId: String, bookingId: String) -> Unit
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text       = stringResource(R.string.flight_details_title),
                        fontSize   = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color      = LocalAppColors.current.textPrimary,
                        fontFamily = Poppins
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter            = painterResource(id = R.drawable.chevron_left),
                            contentDescription = stringResource(R.string.back),
                            tint               = LocalAppColors.current.textPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(
                        start   = 24.dp,
                        end     = 24.dp,
                        top     = 24.dp,
                        bottom  = 24.dp + WindowInsets.navigationBars
                            .asPaddingValues()
                            .calculateBottomPadding()
                    )
            ) {
                Row(
                    modifier              = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment     = Alignment.CenterVertically
                ) {
                    Text(
                        text       = stringResource(R.string.checkin_status_label),
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color      = LocalAppColors.current.textSecondary
                    )
                    Text(
                        text = when {
                            !isOnline -> stringResource(R.string.offline_badge)
                            booking.status == CheckInStatus.CHECK_IN_OPEN ->
                                stringResource(R.string.checkin_status_available)
                            else -> booking.status.name
                        },
                        fontSize   = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color      = when {
                            !isOnline -> ErrorRed
                            booking.status == CheckInStatus.CHECK_IN_OPEN -> ActiveGreen
                            else -> LocalAppColors.current.textPrimary
                        }
                    )
                }

                // Bannière offline si nécessaire
                if (!isOnline) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text       = stringResource(R.string.offline_checkin_unavailable),
                        fontSize   = 12.sp,
                        color      = LocalAppColors.current.textSecondary,
                        fontWeight = FontWeight.Normal
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        val passengerId = booking.passengers.firstOrNull()?.passengerId ?: ""
                        val bookingId   = booking.bookingId
                        onStartCheckIn(passengerId, bookingId)
                    },
                    enabled  = isOnline && booking.status == CheckInStatus.CHECK_IN_OPEN,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor         = NavyBlue,
                        contentColor           = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = NavyBlue.copy(alpha = 0.4f),
                        disabledContentColor   = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.6f)
                    )
                ) {
                    Text(
                        text          = stringResource(R.string.start_checkin),
                        fontSize      = 16.sp,
                        fontWeight    = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            FlightInfoCard(booking)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text       = stringResource(R.string.schedule_label),
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = LocalAppColors.current.textPrimary,
                fontFamily = Poppins
            )
            Spacer(modifier = Modifier.height(16.dp))
            ScheduleTimeline(booking)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FlightDetailsScreenPreview() {
    FlightDetailsScreenContent(
        booking = Booking(
            bookingId  = "b1",
            bookingRef = "BB9XC2",
            pnr        = "BB9XC2",
            lastName   = "Fatma",
            status     = CheckInStatus.CHECK_IN_OPEN,
            gate       = "G24",
            passengers = listOf(
                Passenger(
                    passengerId    = "passenger-fatma-001",
                    uid            = "u1",
                    firstName      = "Djerfi",
                    lastName       = "Fatma",
                    passportNumber = "AB123456",
                    nationality    = "Algerian",
                    dateOfBirth    = "1990-01-01",
                    expiryDate     = null,
                    seatNumber     = "12A",
                    checkinStatus  = "PENDING"
                )
            ),
            flight = Flight(
                flightId         = "f1",
                flightNumber     = "AH 1042",
                origin           = "ALG",
                originCity       = "Algiers",
                destination      = "CDG",
                destinationCity  = "Paris",
                departureTime    = System.currentTimeMillis() + 86_400_000L,
                arrivalTime      = System.currentTimeMillis() + 90_000_000L,
                checkInOpensTime = "06:15",
                boardingTime     = "07:50",
                aircraftType     = "Boeing 737",
                status           = "Scheduled"
            )
        ),
        isOnline       = true,
        onBack         = {},
        onStartCheckIn = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun FlightDetailsScreenOfflinePreview() {
    FlightDetailsScreenContent(
        booking = Booking(
            bookingId  = "b1",
            bookingRef = "BB9XC2",
            pnr        = "BB9XC2",
            lastName   = "Fatma",
            status     = CheckInStatus.CHECK_IN_OPEN,
            gate       = "G24",
            passengers = emptyList(),
            flight = Flight(
                flightId         = "f1",
                flightNumber     = "AH 1042",
                origin           = "ALG",
                originCity       = "Algiers",
                destination      = "CDG",
                destinationCity  = "Paris",
                departureTime    = System.currentTimeMillis() + 86_400_000L,
                arrivalTime      = System.currentTimeMillis() + 90_000_000L,
                checkInOpensTime = "06:15",
                boardingTime     = "07:50",
                aircraftType     = "Boeing 737",
                status           = "Scheduled"
            )
        ),
        isOnline       = false,
        onBack         = {},
        onStartCheckIn = { _, _ -> }
    )
}