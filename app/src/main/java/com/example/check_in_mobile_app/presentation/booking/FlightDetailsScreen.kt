package com.example.check_in_mobile_app.presentation.booking

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlightDetailsScreen(
    booking: Booking,
    onBack: () -> Unit = {},
    onStartCheckIn: () -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Flight Details",
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
                            contentDescription = "Back",
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
                        text = "Check-In Status:",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = MediumGray
                    )
                    Text(
                        text = if (booking.status == CheckInStatus.CHECK_IN_OPEN) "Available Now" else booking.status.name,
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
                        text = "Start Check-In",
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
                .background(Color(0xFFF8FAFC)) // very light gray background like in the image
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // -- TOP CARD: Flight Info --
            FlightInfoCard(booking)

            Spacer(modifier = Modifier.height(24.dp))

            // -- PASSENGER SECTION --
            Text(
                text = "Passenger",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = DarkText,
                fontFamily = Poppins
            )
            Spacer(modifier = Modifier.height(12.dp))
            PassengerCard(booking)

            Spacer(modifier = Modifier.height(24.dp))

            // -- SCHEDULE SECTION --
            Text(
                text = "Schedule",
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
    FlightDetailsScreen(
        booking = Booking(
            bookingRef = "BB9XC2",
            flightNumber = "UA2402",
            origin = "SFO",
            originCity = "San Francisco",
            destination = "JFK",
            destinationCity = "New York",
            departureDate = "Oct 24, 2023",
            departureTime = "08:45",
            duration = "5H 45M",
            passengerName = "Djerfi Fatma",
            pnr = "BB9XC2",
            checkInOpensTime = "06:15",
            boardingTime = "08:00",
            gate = "G24",
            status = CheckInStatus.CHECK_IN_OPEN
        )
    )
}
