package com.example.check_in_mobile_app.presentation.navigation

sealed class Destination(val route: String) {
    object Home : Destination("home")
    object Booking : Destination("booking")
    object AllBookings : Destination("all_bookings")
    object FlightDetails : Destination("flight_details/{bookingRef}") {
        fun createRoute(bookingRef: String) = "flight_details/$bookingRef"
    }
    object PassportScan : Destination("passport_scan")
    object Baggage : Destination("baggage/{passengerId}") {
        fun routeWithArg(passengerId: String) = "baggage/$passengerId"
    }
    object CheckingDetailsReview : Destination("checking_details_review")
    object Register : Destination("register")
    object Login : Destination("login")
    object Splash : Destination("splash")
    object Welcome : Destination("welcome")
    object Selection : Destination("seat-select")
    object Profile : Destination("profile")
    object Notifications : Destination("notifications")
    object preference : Destination("preference/{passengerId}") {
        fun routeWithArg(passengerId: String) = "preference/$passengerId"
    }
    object Confirmation : Destination("confirmation/{passengerId}/{checkedBaggage}/{specialEquipment}") {
        fun routeWithArg(passengerId: String, checkedBaggage: Int, specialEquipment: Int) =
            "confirmation/$passengerId/$checkedBaggage/$specialEquipment"
    }
    object Boarding : Destination("boarding/{passengerId}") {
        fun createRoute(passengerId: String) = "boarding/$passengerId"
    }
}