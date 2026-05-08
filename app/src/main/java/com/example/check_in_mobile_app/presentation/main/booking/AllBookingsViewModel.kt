package com.example.check_in_mobile_app.presentation.main.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Booking
import com.example.domain.usecase.booking.SearchBookingsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class AllBookingsViewModel @Inject constructor(
    private val searchBookingsUseCase: SearchBookingsUseCase
) : ViewModel() {

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
        // We use the use case even for the initial load
        allBookingsAmount.value = searchBookingsUseCase()
    }

    val filteredBookings: StateFlow<List<Booking>> = combine(
        _searchQuery,
        _selectedDate,
        _selectedStatus
    ) { query, date, status ->
        // The business logic of "matching" is now in the Use Case
        searchBookingsUseCase(query, date, status)
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


