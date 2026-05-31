package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.check_in_mobile_app.di.NetworkMonitor
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.usecase.booking.SearchBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject
import kotlinx.coroutines.launch

@HiltViewModel
class AllBookingsViewModel @Inject constructor(
    private val searchBookingsUseCase: SearchBookingsUseCase,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    val isOnline: StateFlow<Boolean> = networkMonitor.isOnline
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = networkMonitor.currentlyOnline()
        )

    // Base data
    private val allBookingsAmount = MutableStateFlow<List<Booking>>(emptyList())

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    // Filters
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery

    private val _selectedDate = MutableStateFlow<String?>(null)
    val selectedDate: StateFlow<String?> = _selectedDate

    private val _selectedStatus = MutableStateFlow("All")
    val selectedStatus: StateFlow<String> = _selectedStatus

    init {
        loadBookings()
    }

    private fun loadBookings() {
        viewModelScope.launch {
            _isLoading.value = true
            val result = searchBookingsUseCase()
            allBookingsAmount.value = result.getOrDefault(emptyList())
            _isLoading.value = false
        }
    }

    val filteredBookings: StateFlow<List<Booking>> = combine(
        allBookingsAmount,
        _searchQuery,
        _selectedDate,
        _selectedStatus
    ) { bookings, query, date, status ->
        bookings
            .filter { booking ->
                val isValidStatus = when (booking.status) {
                    CheckInStatus.CHECKED_IN,
                    CheckInStatus.CONFIRMED,
                    CheckInStatus.CHECK_IN_OPEN -> true
                    CheckInStatus.PASSED -> {
                        val diffMs = System.currentTimeMillis() - booking.flight.departureTime
                        diffMs <= 3 * 24 * 60 * 60 * 1000L
                    }
                }
                if (!isValidStatus) return@filter false

                val matchesQuery = query.isBlank() || 
                                   booking.flight.destinationCity.contains(query, ignoreCase = true) ||                            
                                   booking.flight.destination.contains(query, ignoreCase = true)
                
                val sdfDate = java.text.SimpleDateFormat("MMM dd, yyyy", java.util.Locale.getDefault()).apply {
                    timeZone = java.util.TimeZone.getTimeZone("UTC")
                }
                val depDateStr = sdfDate.format(java.util.Date(booking.flight.departureTime))
                val matchesDate = date == null || depDateStr == date

                val matchesStatus = status == "All" ||
                        booking.status.name.replace("_", " ").equals(status, ignoreCase = true)

                matchesQuery && matchesDate && matchesStatus
            }
            .sortedByDescending { it.flight.departureTime }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun updateSelectedDate(date: String?) {
        _selectedDate.value = date
    }

    fun updateSelectedStatus(status: String) {
        _selectedStatus.value = status
    }
}