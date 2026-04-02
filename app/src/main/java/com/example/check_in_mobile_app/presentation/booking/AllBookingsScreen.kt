package com.example.check_in_mobile_app.presentation.booking

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.res.painterResource
import com.example.check_in_mobile_app.R
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.check_in_mobile_app.presentation.components.booking.BookingCard
import com.example.check_in_mobile_app.presentation.components.booking.DateField
import com.example.check_in_mobile_app.presentation.components.booking.FilterChipsRow
import com.example.check_in_mobile_app.presentation.components.booking.SearchField
import com.example.check_in_mobile_app.ui.theme.NavyBlue

@Composable
fun AllBookingsScreen(
    onNavigateBack: () -> Unit = {},
    viewModel: AllBookingsViewModel = viewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedDate by viewModel.selectedDate.collectAsState()
    val selectedStatus by viewModel.selectedStatus.collectAsState()
    val filteredBookings by viewModel.filteredBookings.collectAsState()

    AllBookingsScreenContent(
        onNavigateBack = onNavigateBack,
        searchQuery = searchQuery,
        onSearchQueryChange = viewModel::updateSearchQuery,
        selectedDate = selectedDate,
        onDateSelected = viewModel::updateSelectedDate,
        onClearDate = { viewModel.updateSelectedDate(null) },
        selectedStatus = selectedStatus,
        onStatusSelect = viewModel::updateSelectedStatus,
        filteredBookings = filteredBookings
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllBookingsScreenContent(
    onNavigateBack: () -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedDate: String?,
    onDateSelected: (String) -> Unit,
    onClearDate: () -> Unit = {},
    selectedStatus: String,
    onStatusSelect: (String) -> Unit,
    filteredBookings: List<Booking>
) {
    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "All Bookings",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = NavyBlue
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(painter = painterResource(id = R.drawable.chevron_left), contentDescription = "Back", tint = NavyBlue)
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
                modifier = Modifier.padding(horizontal = 20.dp)
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
            val statuses = listOf("All", "Confirmed", "Check In open", "Checked In", "Passed")
            FilterChipsRow(
                statuses = statuses,
                selectedStatus = selectedStatus,
                onStatusSelect = onStatusSelect,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "All flights",
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
                    BookingCard(booking = booking)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AllBookingsScreenPreview() {
    val searchQuery = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("") }
    val selectedDate = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf<String?>(null) }
    val selectedStatus = androidx.compose.runtime.remember { androidx.compose.runtime.mutableStateOf("All") }
    
    val allBookings = com.example.domain.usecase.booking.GetUpcomingBookingsUseCase(
        com.example.data.repository.BookingRepositoryImpl()
    ).invoke()

    val filteredBookings = allBookings.filter { booking ->
        val matchesQuery = searchQuery.value.isBlank() || 
                           booking.destinationCity.contains(searchQuery.value, ignoreCase = true) || 
                           booking.originCity.contains(searchQuery.value, ignoreCase = true) ||
                           booking.destination.contains(searchQuery.value, ignoreCase = true)
        val matchesDate = selectedDate.value == null || booking.departureDate == selectedDate.value
        val matchesStatus = selectedStatus.value == "All" || booking.status.name.replace("_", " ").equals(selectedStatus.value, ignoreCase = true)
        
        matchesQuery && matchesDate && matchesStatus
    }
    
    AllBookingsScreenContent(
        onNavigateBack = {},
        searchQuery = searchQuery.value,
        onSearchQueryChange = { searchQuery.value = it },
        selectedDate = selectedDate.value,
        onDateSelected = { selectedDate.value = it },
        selectedStatus = selectedStatus.value,
        onStatusSelect = { selectedStatus.value = it },
        filteredBookings = filteredBookings
    )
}
