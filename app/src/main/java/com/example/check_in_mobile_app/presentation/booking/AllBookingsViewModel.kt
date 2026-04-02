package com.example.check_in_mobile_app.presentation.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.repository.BookingRepositoryImpl
import com.example.domain.model.Booking
import com.example.domain.usecase.booking.GetUpcomingBookingsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AllBookingsViewModel : ViewModel() {

    private val useCase = GetUpcomingBookingsUseCase(BookingRepositoryImpl())
    
    // Base data
    private val allBookingsAmount = MutableStateFlow<List<Booking>>(emptyList())

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
        allBookingsAmount.value = useCase()
    }

    val filteredBookings: StateFlow<List<Booking>> = combine(
        allBookingsAmount,
        _searchQuery,
        _selectedDate,
        _selectedStatus
    ) { bookings, query, date, status ->
        bookings.filter { booking ->
            val matchesQuery = query.isBlank() || 
                booking.destinationCity.contains(query, ignoreCase = true) || 
                booking.originCity.contains(query, ignoreCase = true) ||
                booking.destination.contains(query, ignoreCase = true)
            
            val matchesDate = date == null || booking.departureDate == date
            
            val matchesStatus = status == "All" || booking.status.name.replace("_", " ").equals(status, ignoreCase = true)

            matchesQuery && matchesDate && matchesStatus
        }
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
