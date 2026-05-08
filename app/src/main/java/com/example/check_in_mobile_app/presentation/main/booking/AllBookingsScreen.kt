package com.example.check_in_mobile_app.presentation.main.booking


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.res.painterResource
import com.example.check_in_mobile_app.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.domain.model.Booking
import com.example.check_in_mobile_app.presentation.components.booking.BookingCard
import com.example.check_in_mobile_app.presentation.components.booking.DateField
import com.example.check_in_mobile_app.presentation.components.booking.FilterChipsRow
import com.example.check_in_mobile_app.presentation.components.booking.SearchField
import com.example.check_in_mobile_app.ui.theme.NavyBlue
import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.model.CheckInStatus
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

import androidx.compose.ui.res.stringResource

@Composable
fun AllBookingsScreen(
    onNavigateBack: () -> Unit = {},
    onBoarding: () -> Unit = {},
    onCheckInClick: (String) -> Unit = {},
    viewModel: AllBookingsViewModel = hiltViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val filteredBookings by viewModel.filteredBookings.collectAsState()

    AllBookingsScreenContent(
        onNavigateBack = onNavigateBack,
        onBoarding = onBoarding,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::updateSearchQuery,
        selectedDate = selectedDate,
        onDateSelected = viewModel::updateSelectedDate,
        onClearDate = { viewModel.updateSelectedDate(null) },
        selectedStatus = selectedStatus,
        onStatusSelect = viewModel::updateSelectedStatus,
        filteredBookings = filteredBookings,
        onCheckInClick = onCheckInClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBookingsScreenContent(
    onNavigateBack: () -> Unit,
    onBoarding: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedDate: String?,
    onDateSelected: (String) -> Unit,
    onClearDate: () -> Unit = {},
    selectedStatus: String,
    onStatusSelect: (String) -> Unit,
    filteredBookings: List<Booking>,
    onCheckInClick: (String) -> Unit = {}
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.all_bookings),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(id = R.drawable.chevron_left), contentDescription = stringResource(R.string.back), tint = NavyBlue)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            HorizontalDivider(color = Color(0xFFF1F5F9), thickness = 1.dp)
            
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            SearchField(
                query = searchQuery,
                onQueryChange = onSearchQueryChange,
                modifier = Modifier.padding(horizontal = 20.dp),
                placeholderText = stringResource(R.string.search_hint)
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date Picker Field
            DateField(
                selectedDate = selectedDate,
                onDateSelected = onDateSelected,
                onClearDate = onClearDate,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Filters
            val statusOptions = listOf(
                "All" to stringResource(R.string.status_all),
                "Confirmed" to stringResource(R.string.status_confirmed),
                "Check In open" to stringResource(R.string.status_check_in_open),
                "Checked In" to stringResource(R.string.status_checked_in),
                "Passed" to stringResource(R.string.status_passed)
            )

            FilterChipsRow(
                options = statusOptions,
                selectedStatusValue = selectedStatus,
                onStatusSelect = onStatusSelect,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = stringResource(R.string.all_flights),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = NavyBlue,
                modifier = Modifier.padding(horizontal = 16.dp)
            )


            Spacer(modifier = Modifier.height(12.dp))

            // Bookings List
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredBookings) { booking ->
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

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllBookingsScreenPreview() {
    val searchQuery = remember { mutableStateOf("") }
    val selectedDate = remember { mutableStateOf<String?>(null) }
    val selectedStatus = remember { mutableStateOf("All") }
    
    val allBookings = emptyList<Booking>()


    val filteredBookings = allBookings.filter { booking ->
        val matchesQuery = searchQuery.value.isBlank() || 
                           booking.flight.destinationCity.contains(searchQuery.value, ignoreCase = true) ||                            
                           booking.flight.destination.contains(searchQuery.value, ignoreCase = true)
        
        val sdfDate = SimpleDateFormat("dd MMM", Locale.getDefault())
        val depDateStr = sdfDate.format(Date(booking.flight.departureTime))
        val matchesDate = selectedDate.value == null || depDateStr == selectedDate.value
        
        val uiStatus = try { CheckInStatus.valueOf(booking.status.name) } catch (e: Exception) { CheckInStatus.CONFIRMED }
        val matchesStatus = selectedStatus.value == "All" || uiStatus.name.replace("_", " ").equals(selectedStatus.value, ignoreCase = true)
        
        matchesQuery && matchesDate && matchesStatus
    }
    
    AllBookingsScreenContent(
        onNavigateBack = {},
        onBoarding = {},
        searchQuery = searchQuery.value,
        onSearchQueryChange = { searchQuery.value = it },
        selectedDate = selectedDate.value,
        onDateSelected = { selectedDate.value = it },
        selectedStatus = selectedStatus.value,
        onStatusSelect = { selectedStatus.value = it },
        filteredBookings = filteredBookings
    )
}
