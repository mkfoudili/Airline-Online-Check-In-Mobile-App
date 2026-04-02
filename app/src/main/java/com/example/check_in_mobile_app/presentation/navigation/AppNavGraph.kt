package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.booking.BookingScreen

@Composable
fun AppNavGraph(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Destination.Booking.route
    ) {
        composable(route = Destination.Booking.route) {
            BookingScreen(
                onViewAllClick = {
                    navController.navigate(Destination.AllBookings.route)
                }
            )
        }
        composable(route = Destination.AllBookings.route) {
            com.example.check_in_mobile_app.presentation.booking.AllBookingsScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavGraphPreview() {
    AppNavGraph()
}