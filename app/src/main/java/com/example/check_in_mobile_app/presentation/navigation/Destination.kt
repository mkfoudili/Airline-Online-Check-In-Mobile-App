package com.example.check_in_mobile_app.presentation.navigation

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Booking : Destination("booking")
    object AllBookings : Destination("all_bookings")
    object FlightDetails : Destination("flight_details/{bookingRef}") {
        fun createRoute(bookingRef: String) = "flight_details/$bookingRef"
    }
}