package com.example.check_in_mobile_app.presentation.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.Passenger

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {},
    onNavigateToBoardingScreen: () -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    val isOnline by viewModel.isOnline.collectAsState()
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(TabItem.HOME) }
    val scrollState = remember(isOnline) { ScrollState(initial = 0) }
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp

    //Simple mock for now
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
                checkinStatus = "PENDING",
                expiryDate = "2025-01-01"
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
        bottomBar = {
            TabBarMenu(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    if (tab != selectedTab) {
                        selectedTab = tab
                        onTabSelected(tab)
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
        ) {
            key(isOnline) {
                if (isOnline) {
                    OnlineHomeScreen(
                        uiState = uiState,
                        onBookingReferenceChange = viewModel::onBookingReferenceChange,
                        onLastNameChange = viewModel::onLastNameChange,
                        onCheckInClick = {
                            viewModel.onCheckInNow()
                            onCheckInClick()
                        },
                        onFindFlightClick = {
                            viewModel.onFindFlight()
                            onFindFlightClick()
                        },
                        screenWidth = screenWidth
                    )
                } else {
                    OfflineHomeScreen(
                        onNavigateToBoardingScreen = onNavigateToBoardingScreen,
                        screenWidth = screenWidth,
                        booking = booking
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}