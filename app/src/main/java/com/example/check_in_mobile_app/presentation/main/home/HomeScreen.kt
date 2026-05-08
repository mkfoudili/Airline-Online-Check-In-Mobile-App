package com.example.check_in_mobile_app.presentation.main.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.domain.model.*

import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCheckInClick: () -> Unit = {},
    onFindFlightClick: () -> Unit = {},
    onNavigateToBoardingScreen: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    // collectAsStateWithLifecycle, unsubscribes when screen is in the background,
    // resubscribes when it comes back to the foreground, triggering re-emission.
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Reset scroll position when connectivity changes so the user sees the
    // top of whichever screen (online / offline) just appeared.
    val scrollState = remember(isOnline) { ScrollState(initial = 0) }
    val screenWidth: Dp = LocalConfiguration.current.screenWidthDp.dp

    // Simple mock booking for now
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
                selectedTab = TabItem.HOME,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
        // PullToRefreshBox wraps the scrollable content.
        // Pulling down triggers onRefresh in the ViewModel which re-checks
        // connectivity and gives the user visual feedback.
        PullToRefreshBox(
            isRefreshing = uiState.isRefreshing,
            onRefresh = viewModel::onRefresh,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                // key(isOnline) forces Compose to tear down and rebuild the
                // child subtree when connectivity changes, so the correct
                // screen is shown without any stale state leaking through.
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
                            onProfileClick = onProfileClick,
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
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}