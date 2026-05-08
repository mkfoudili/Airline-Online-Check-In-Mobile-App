package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.checkin.boarding.BoardingScreen
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.main.booking.AllBookingsScreen
import com.example.check_in_mobile_app.presentation.main.booking.BookingScreen
import com.example.check_in_mobile_app.presentation.main.booking.FlightDetailsScreen
import com.example.check_in_mobile_app.presentation.main.home.HomeScreen
import com.example.check_in_mobile_app.presentation.main.notifications.NotificationsScreen
import com.example.check_in_mobile_app.presentation.main.profile.ProfileScreen
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.check_in_mobile_app.presentation.main.booking.FlightDetailsViewModel


@Composable
fun MainNavGraph(
    onCheckInClick: (String) -> Unit,
    navigateToHome: State<Boolean> = mutableStateOf(false),
    onNavigateToHomeHandled: () -> Unit = {}
) {
    val navController = rememberNavController()

    LaunchedEffect(navigateToHome.value) {
        if (navigateToHome.value) {
            navController.navigate(Destination.Home.route) {
                popUpTo(Destination.Home.route) { inclusive = true }
                launchSingleTop = true
            }
            onNavigateToHomeHandled()
        }
    }

    val navigateToTab: (TabItem) -> Unit = { tab ->
        val route = when (tab) {
            TabItem.HOME -> Destination.Home.route
            TabItem.TICKETS -> Destination.Booking.route
            TabItem.NOTIFICATIONS -> Destination.Notifications.route
            TabItem.PROFILE -> Destination.Profile.route
        }
        navController.navigate(route) {
            popUpTo(Destination.Home.route) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Home.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Destination.Home.route) {
            HomeScreen(
                onTabSelected = navigateToTab,
                onNavigateToBoardingScreen = {
                    navController.navigate(Destination.Boarding.route)
                },
                onProfileClick = { navigateToTab(TabItem.PROFILE) }
            )
        }
        composable(Destination.Booking.route) {
            BookingScreen(
                onViewAllClick = { navController.navigate(Destination.AllBookings.route) },
                onTabSelected = navigateToTab,
                onCheckInClick = { bookingRef ->
                    navController.navigate(Destination.FlightDetails.createRoute(bookingRef))
                },
                onBoarding = { navController.navigate(Destination.Boarding.route) }
            )
        }
        composable(
            route = Destination.AllBookings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            AllBookingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onBoarding = { navController.navigate(Destination.Boarding.route) }
            )
        }
        composable(Destination.FlightDetails.route) { backStackEntry ->
            val bookingRef = backStackEntry.arguments?.getString("bookingRef") ?: ""
            val viewModel: FlightDetailsViewModel = hiltViewModel()
            val booking = viewModel.booking ?: return@composable

            FlightDetailsScreen(
                booking = booking,
                onBack = { navController.popBackStack() },
                onStartCheckIn = { onCheckInClick(bookingRef) }
            )
        }
        composable(Destination.Boarding.route) {
            BoardingScreen(
                onBack = { navController.popBackStack() }
            )
        }
        composable(Destination.Profile.route) {
            ProfileScreen(
                onTabSelected = navigateToTab,
                onLogout = {  }
            )
        }
        composable(Destination.Notifications.route) {
            NotificationsScreen(
                onTabSelected = navigateToTab
            )
        }
    }
}