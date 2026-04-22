package com.example.check_in_mobile_app.presentation.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem


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
    val screenWidth = with(LocalDensity.current) {
        LocalWindowInfo.current.containerSize.width.toDp()
    }

    // Simple mock for now
//    val booking = Booking(
//        bookingRef = "00001",
//        flightNumber = "UA2402",
//        origin = "SFO",
//        destination = "JFK",
//        departureDate = "2024-10-24",
//        departureTime = "15:30",
//        boardingTime = "14:30",
//        status = CheckInStatus.CHECK_IN_OPEN,
//        duration = "5h 45m",
//        originCity = "San Francisco",
//        destinationCity = "New York",
//    )

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
//                    OfflineHomeScreen(
//                        onNavigateToBoardingScreen = onNavigateToBoardingScreen,
//                        screenWidth = screenWidth,
//                        booking = booking
//                    )
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