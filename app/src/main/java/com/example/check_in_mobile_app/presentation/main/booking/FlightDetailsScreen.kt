package com.example.check_in_mobile_app.presentation.main.booking

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
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.ui.theme.*
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.presentation.components.flightdetails.PassengerCard
import com.example.check_in_mobile_app.presentation.components.flightdetails.ScheduleTimeline
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.Passenger
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    onBack: () -> Unit = {},
    onStartCheckIn: () -> Unit = {},
    viewModel: FlightDetailsViewModel = hiltViewModel()
) {
    val booking by viewModel.booking.collectAsStateWithLifecycle()

    if (booking == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    FlightDetailsScreenContent(
        booking = booking!!,
        onBack = onBack,
        onStartCheckIn = onStartCheckIn
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreenContent(
    booking: Booking,
    onBack: () -> Unit,
    onStartCheckIn: () -> Unit
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.flight_details_title),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkText,
                        fontFamily = Poppins
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(id = R.drawable.chevron_left),
                            contentDescription = stringResource(R.string.back),
                            tint = DarkText
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(
                        start = 24.dp,
                        end = 24.dp,
                        top = 24.dp,
                        bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.checkin_status_label),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MediumGray
                    )
                    Text(
                        text = if (booking.status == CheckInStatus.CHECK_IN_OPEN) stringResource(R.string.checkin_status_available) else booking.status.name,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (booking.status == CheckInStatus.CHECK_IN_OPEN) ActiveGreen else DarkText
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onStartCheckIn,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = NavyBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = stringResource(R.string.start_checkin),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        letterSpacing = (-0.2).sp
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            FlightInfoCard(booking)
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.passenger_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = Poppins
            )
            Spacer(modifier = Modifier.height(12.dp))
            PassengerCard(booking, infoText = "${stringResource(R.string.pnr_label)} ${booking.pnr}")
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(R.string.schedule_label),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
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
            bookingId = "b1",
            bookingRef = "BB9XC2",
            pnr = "BB9XC2",
            lastName = "Fatma",
            status = CheckInStatus.CHECK_IN_OPEN,
            gate = "G24",
            passengers = listOf(
                Passenger(
                    passengerId = "p1",
                    uid = "u1",
                    firstName = "Djerfi",
                    lastName = "Fatma",
                    passportNumber = "AB123456",
                    nationality = "Algerian",
                    dateOfBirth = "1990-01-01",
                    expiryDate = null,
                    seatNumber = "12A",
                    checkinStatus = "PENDING"
                )
            ),
            flight = Flight(
                flightId = "f1",
                flightNumber = "UA2402",
                origin = "SFO",
                originCity = "San Francisco",
                destination = "JFK",
                destinationCity = "New York",
                departureTime = System.currentTimeMillis() + 86400000,
                arrivalTime = System.currentTimeMillis() + 90000000,
                checkInOpensTime = "06:15",
                boardingTime = "08:00",
                aircraftType = "Boeing 737",
                status = "Scheduled"
            )
        ),
        onBack = {},
        onStartCheckIn = {}
    )
}
