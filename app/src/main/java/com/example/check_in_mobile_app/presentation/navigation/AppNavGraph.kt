package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.booking.BookingScreen
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.home.HomeScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    val navigateToTab: (TabItem) -> Unit = { tab ->
        val route = when (tab) {
            TabItem.HOME -> Destination.Home.route
            TabItem.TICKETS -> Destination.Booking.route
            else -> null
        }
        if (route != null) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) { saveState = true }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Home.route
    ) {
        composable(route = Destination.Home.route) {
            HomeScreen(
                onTabSelected = navigateToTab
            )
        }

        composable(route = Destination.Booking.route) {
            BookingScreen(
                onViewAllClick = {
                    navController.navigate(Destination.AllBookings.route)
                },
                onTabSelected = navigateToTab,
                onCheckInClick = { bookingRef ->
                    navController.navigate(Destination.FlightDetails.createRoute(bookingRef))
                }
            )
        }
        composable(route = Destination.AllBookings.route) {
            com.example.check_in_mobile_app.presentation.booking.AllBookingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        
        composable(route = Destination.FlightDetails.route) { backStackEntry ->
            val bookingRef = backStackEntry.arguments?.getString("bookingRef") ?: ""
            // Simple mock extraction for UI logic
            val booking = com.example.data.repository.BookingRepositoryImpl()
                .getUpcomingBookings()
                .find { it.bookingRef == bookingRef } 
                ?: com.example.data.repository.BookingRepositoryImpl().getUpcomingBookings().first()
                
            com.example.check_in_mobile_app.presentation.booking.FlightDetailsScreen(
                booking = booking,
                onBack = { navController.popBackStack() },
                onStartCheckIn = {
                    // navController.navigate(Destination.Boarding.route) // Handle navigation later
                }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavGraphPreview() {
    AppNavGraph()
}