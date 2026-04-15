package com.example.check_in_mobile_app.presentation.checkin.confirmation

import android.widget.Toast
import android.widget.Toast.makeText
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.check_in_mobile_app.R
import com.example.check_in_mobile_app.presentation.components.flightdetails.FlightInfoCard
import com.example.check_in_mobile_app.presentation.components.flightdetails.PassengerCard
import com.example.check_in_mobile_app.ui.theme.ConfirmationGreen
import com.example.check_in_mobile_app.ui.theme.CoolGray
import com.example.check_in_mobile_app.ui.theme.DividerColor
import com.example.check_in_mobile_app.ui.theme.InfoGray
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.Passenger

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmationScreen(
    onNavigateToHomeScreen: () -> Unit = {}
) {
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp
    val context = LocalContext.current

    // Simple mock for now
    val booking = Booking(
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
    )

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Check-In Completed",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Box(
                modifier = Modifier
                    .requiredWidth(screenWidth)
                    .height(1.dp)
                    .background(DividerColor)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color.White, shape = CircleShape)
                        .border(2.dp, Color(0x332EE8AA), shape = CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.circle_check),
                        contentDescription = "Profile",
                        tint = ConfirmationGreen,
                        modifier = Modifier.size(40.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "You're All Set!",
                    fontSize = 26.sp,
                    color = ConfirmationGreen,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.sp,
                    lineHeight = 32.sp
                )

                Text(
                    text = "Your check-in was successful. You can now view your boarding pass or download a PDF copy.",
                    fontSize = 16.sp,
                    color = InfoGray,
                    fontWeight = FontWeight.Normal,
                    letterSpacing = 0.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier.padding(horizontal = 32.dp, vertical = 12.dp),
                    textAlign = TextAlign.Center
                )
            }

            FlightInfoCard(
                booking = booking
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PASSENGER DETAILS",
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = CoolGray,
                letterSpacing = 0.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            PassengerCard(
                booking = booking,
                infoText = "Frequent Flyer: ${booking.passengers[0].passengerId}"
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                InfoCard(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            NavyBlue,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(Color.White)
                        .height(91.dp),
                    title = "SEAT",
                    value = "${booking.passengers[0].seatNumber}",
                    iconResId = com.example.data.R.drawable.briefcase,
                    iconDescription = "Seat Icon"
                )

                Spacer(modifier = Modifier.width(16.dp))

                InfoCard(
                    modifier = Modifier
                        .weight(1f)
                        .border(
                            1.dp,
                            NavyBlue,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .background(Color.White)
                        .height(91.dp),
                    title = "BAGGAGE",
                    value = "01",
                    iconResId = com.example.data.R.drawable.briefcase,
                    iconDescription = "Baggage Icon"
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { // TODO: Implement PDF generation and downloading logic,
                    makeText(context, "Download PDF clicked", Toast.LENGTH_SHORT).show()
                },
                enabled = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .height(52.dp)
                    .border(1.dp, NavyBlue, shape = RoundedCornerShape(14.dp)),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.download),
                    contentDescription = "Download icon",
                    tint = NavyBlue,
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Download PDF",
                    color = NavyBlue,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onNavigateToHomeScreen,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp)
                    .height(52.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF3F5FB))
            ) {
                Text(
                    text = "Back to Dashboard",
                    color = NavyBlue,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier,
    title: String,
    value: String,
    iconResId: Int,
    iconDescription : String = ""
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            painter = painterResource(id = iconResId),
            contentDescription = iconDescription,
            tint = InfoGray,
        )

        Text(
            text = title,
            fontSize = 10.sp,
            color = InfoGray,
            fontWeight = FontWeight.Light,
            letterSpacing = 0.sp,
            lineHeight = 15.sp
        )

        Text(
            text = value,
            fontSize = 18.sp,
            color = NavyBlue,
            fontWeight = FontWeight.ExtraBold,
            letterSpacing = 0.sp,
            lineHeight = 28.sp
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewConfirmationScreen() {
    ConfirmationScreen()
}