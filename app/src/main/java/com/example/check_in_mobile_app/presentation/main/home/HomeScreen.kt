package com.example.check_in_mobile_app.presentation.main.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.check_in_mobile_app.presentation.components.TabBarMenu
import com.example.check_in_mobile_app.presentation.components.TabItem
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.check_in_mobile_app.presentation.utils.rememberScreenWidth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onCheckInClick: () -> Unit = {},
    onNavigateToFlightDetails: (String) -> Unit = {},
    onNavigateToBoardingScreen: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTabSelected: (TabItem) -> Unit = {}
) {
    val isOnline by viewModel.isOnline.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollState = remember(isOnline) { ScrollState(initial = 0) }
    val screenWidth: Dp = rememberScreenWidth()

    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            TabBarMenu(
                selectedTab = TabItem.HOME,
                onTabSelected = onTabSelected
            )
        }
    ) { innerPadding ->
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
                            onFindFlightClick = viewModel::onFindFlight,
                            onProfileClick = onProfileClick,
                            onNavigateToFlightDetails = onNavigateToFlightDetails,
                            screenWidth = screenWidth
                        )
                    } else {
                        OfflineHomeScreen(
                            onNavigateToBoardingScreen = onNavigateToBoardingScreen,
                            screenWidth = screenWidth,
                            uiState = uiState
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}