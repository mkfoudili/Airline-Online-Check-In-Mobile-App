package com.example.check_in_mobile_app.presentation.navigation

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Booking : Destination("booking")
    object AllBookings : Destination("all_bookings")
    object FlightDetails : Destination("flight_details/{bookingRef}") {
        fun createRoute(bookingRef: String) = "flight_details/$bookingRef"
    }
    object Boarding : Destination("boarding")
    object PassportScan : Destination("passport_scan")
    object Baggage : Destination("baggage")
    object Register: Destination("register")
    object Login : Destination("login")
    object Splash   : Destination("splash")
    object Welcome  : Destination("welcome")
    object Confirmation : Destination("confirmation")
}
