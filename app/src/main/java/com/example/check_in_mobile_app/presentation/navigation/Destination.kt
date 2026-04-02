package com.example.check_in_mobile_app.presentation.navigation

sealed class Destination(val route: String) {
    object Booking : Destination("booking")
    object AllBookings : Destination("all_bookings")
}