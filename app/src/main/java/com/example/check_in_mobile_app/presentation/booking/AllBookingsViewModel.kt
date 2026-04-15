package com.example.check_in_mobile_app.presentation.booking

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Booking
import com.example.domain.model.CheckInStatus
import com.example.domain.model.Flight
import com.example.domain.model.Passenger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class AllBookingsViewModel : ViewModel() {

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
        val mockFlight = Flight(
            flightId = "f1",
            flightNumber = "SW402",
            origin = "LHR",
            originCity = "London",
            destination = "CDG",
            destinationCity = "Paris",
            departureTime = System.currentTimeMillis() + 86400000,
            arrivalTime = System.currentTimeMillis() + 90000000,
            checkInOpensTime = "06:15",
            boardingTime = "08:00",
            aircraftType = "Boeing 737",
            status = "Scheduled"
        )
        val mockPassenger = Passenger(
            passengerId = "p1",
            uid = "u1",
            firstName = "Djerfi",
            lastName = "Fatma",
            passportNumber = "AB123456",
            nationality = "Algerian",
            dateOfBirth = "1990-01-01",
            seatNumber = "12A",
            checkinStatus = "PENDING"
        )
        allBookingsAmount.value = listOf(
            Booking(
                bookingId = "BB9XC2",
                pnr = "BB9XC2",
                lastName = "Fatma",
                status = CheckInStatus.CONFIRMED,
                flight = mockFlight,
                passengers = listOf(mockPassenger),
                gate = "G24"
            ),
            Booking(
                bookingId = "AA1BB2",
                pnr = "AA1BB2",
                lastName = "Fatma",
                status = CheckInStatus.CHECK_IN_OPEN,
                flight = mockFlight.copy(flightNumber = "SW405"),
                passengers = listOf(mockPassenger),
                gate = "H12"
            )
        )
    }

    val filteredBookings: StateFlow<List<Booking>> = combine(
        allBookingsAmount,
        _searchQuery,
        _selectedDate,
        _selectedStatus
    ) { bookings, query, date, status ->
        bookings.filter { booking ->
            val matchesQuery = query.isBlank() || 
                booking.flight.destinationCity.contains(query, ignoreCase = true) || 
                booking.flight.destination.contains(query, ignoreCase = true) ||
                booking.flight.originCity.contains(query, ignoreCase = true) ||
                booking.flight.origin.contains(query, ignoreCase = true)
            
            val sdfDate = java.text.SimpleDateFormat("dd MMM", java.util.Locale.getDefault())
            val depDateStr = sdfDate.format(java.util.Date(booking.flight.departureTime))
            val matchesDate = date == null || depDateStr == date
            
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
