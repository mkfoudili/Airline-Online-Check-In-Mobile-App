package com.example.check_in_mobile_app.presentation.main.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.tooling.preview.Preview
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.check_in_mobile_app.presentation.components.booking.BookingCard
import com.example.check_in_mobile_app.presentation.components.booking.ViewAllButton
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.ui.theme.DarkText
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.check_in_mobile_app.ui.theme.Poppins
import com.example.domain.model.Flight

import androidx.compose.ui.res.stringResource
import com.example.check_in_mobile_app.R

import androidx.compose.material3.*
import androidx.compose.runtime.*
import com.example.check_in_mobile_app.presentation.main.MainActivity

@Composable
fun BookingScreen(
    viewModel: BookingViewModel = viewModel(),
    onViewAllClick: () -> Unit = {},
    onBoarding: () -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {},
    onCheckInClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    BookingScreenContent(
        uiState = uiState,
        onViewAllClick = onViewAllClick,
        onBoarding = onBoarding,
        onTabSelected = onTabSelected,
        onCheckInClick = onCheckInClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingScreenContent(
    uiState: BookingUiState,
    onViewAllClick: () -> Unit = {},
    onBoarding: () -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {},
    onCheckInClick: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.my_bookings),
                        fontFamily = Poppins,
                        fontSize = 25.sp,
                        color = DarkText,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = (-0.5).sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },

        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.TICKETS,
                onTabSelected = onTabSelected
            )
        }

    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            when (val state = uiState) {
                is BookingUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = NavyBlue)
                    }
                }

                is BookingUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = state.message,
                            color = NavyBlue,
                            fontSize = 14.sp
                        )
                    }
                }

                is BookingUiState.Success -> {
                    ViewAllButton(
                        title = stringResource(R.string.upcoming_flights),
                        actionLabel = stringResource(R.string.view_all),
                        onActionClick = onViewAllClick
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    ) {
                        items(state.bookings) { booking ->
                            BookingCard(
                                booking = booking,
                                onCheckInClick = onCheckInClick,
                                onBoarding = onBoarding
                            )
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookingScreenPreview() {
    val mockFlight = Flight(
        flightId = "f1",
        flightNumber = "BA342",
        origin = "LHR",
        originCity = "SAN FRANCISCO",
        destination = "CDG",
        destinationCity = "LONDON",
        departureTime = System.currentTimeMillis() + 86400000,
        arrivalTime = System.currentTimeMillis() + 90000000 + (11 * 60 * 60 * 1000) + (20 * 60 * 1000), // ~11h 20m later
        checkInOpensTime = "06:15",
        boardingTime = "22:15",
        aircraftType = "Boeing 737",
        status = "Scheduled"
    )

    val dummyBookings = listOf(
        Booking(
            bookingId = "b1",
            pnr = "REF001",
            lastName = "Smith",
            status = CheckInStatus.CHECK_IN_OPEN,
            flight = mockFlight,
            passengers = emptyList(),
            bookingRef = "REF001"
        ),
        Booking(
            bookingId = "b2",
            pnr = "REF002",
            lastName = "Smith",
            status = CheckInStatus.CHECK_IN_OPEN,
            flight = mockFlight,
            passengers = emptyList(),
            bookingRef = "REF002"
        ),
        Booking(
            bookingId = "b3",
            pnr = "REF003",
            lastName = "Smith",
            status = CheckInStatus.CONFIRMED,
            flight = mockFlight,
            passengers = emptyList(),
            bookingRef = "REF003"
        ),
        Booking(
            bookingId = "b4",
            pnr = "REF004",
            lastName = "Smith",
            status = CheckInStatus.PASSED,
            flight = mockFlight,
            passengers = emptyList(),
            bookingRef = "REF004"
        )
    )
    BookingScreenContent(
        uiState = BookingUiState.Success(dummyBookings)
    )
}