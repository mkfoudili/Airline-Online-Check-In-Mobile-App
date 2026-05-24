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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.check_in_mobile_app.presentation.auth.AuthViewModel
import com.example.check_in_mobile_app.presentation.checkin.boarding.BoardingScreen
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.main.booking.AllBookingsScreen
import com.example.check_in_mobile_app.presentation.main.booking.BookingScreen
import com.example.check_in_mobile_app.presentation.main.booking.FlightDetailsScreen
import com.example.check_in_mobile_app.presentation.main.home.HomeScreen
import com.example.check_in_mobile_app.presentation.main.notifications.NotificationsScreen
import com.example.check_in_mobile_app.presentation.main.profile.ProfileScreen

@Composable
fun MainNavGraph(
    onCheckInClick: (bookingRef: String, passengerId: String, bookingId: String) -> Unit,
    onLogout: () -> Unit,                          // ← nouveau paramètre
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
            TabItem.HOME          -> Destination.Home.route
            TabItem.TICKETS       -> Destination.Booking.route
            TabItem.NOTIFICATIONS -> Destination.Notifications.route
            TabItem.PROFILE       -> Destination.Profile.route
        }
        navController.navigate(route) {
            popUpTo(Destination.Home.route) {
                saveState = true
                inclusive = false
            }
            launchSingleTop = true
            restoreState    = true
        }
    }

    val navigateToFlightDetails: (String) -> Unit = { bookingRef ->
        navController.navigate(Destination.FlightDetails.createRoute(bookingRef))
    }

    NavHost(
        navController    = navController,
        startDestination = Destination.Home.route,
        enterTransition  = { EnterTransition.None },
        exitTransition   = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {
        composable(Destination.Home.route) {
            HomeScreen(
                onTabSelected              = navigateToTab,
                onNavigateToBoardingScreen = { navController.navigate(Destination.Boarding.createRoute("")) },
                onProfileClick             = { navigateToTab(TabItem.PROFILE) },
                onCheckInClick             = { },
                onNavigateToFlightDetails  = navigateToFlightDetails
            )
        }

        composable(Destination.Booking.route) {
            BookingScreen(
                onViewAllClick = { navController.navigate(Destination.AllBookings.route) },
                onTabSelected  = navigateToTab,
                onCheckInClick = { bookingRef ->
                    navController.navigate(Destination.FlightDetails.createRoute(bookingRef))
                },
                onBoarding = { passengerId ->
                    navController.navigate(Destination.Boarding.createRoute(passengerId))
                }
            )
        }

        composable(
            route = Destination.AllBookings.route,
            enterTransition    = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition     = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition  = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            AllBookingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onBoarding     = { navController.navigate(Destination.Boarding.route) },
                onCheckInClick = { bookingRef ->
                    navController.navigate(Destination.FlightDetails.createRoute(bookingRef))
                }
            )
        }

        composable(
            route      = Destination.FlightDetails.route,
            arguments  = listOf(navArgument("bookingRef") { type = NavType.StringType }),
            enterTransition    = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition     = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition  = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) { backStackEntry ->
            val bookingRef = backStackEntry.arguments?.getString("bookingRef") ?: ""
            FlightDetailsScreen(
                onBack         = { navController.popBackStack() },
                onStartCheckIn = { passengerId, bookingId ->
                    onCheckInClick(bookingRef, passengerId, bookingId)
                }
            )
        }

        composable(
            route = Destination.Boarding.route,
            arguments = listOf(navArgument("passengerId") { type = NavType.StringType })
        ) {
            BoardingScreen(onBack = { navController.popBackStack() })
        }

        composable(Destination.Profile.route) {
            ProfileScreen(
                onTabSelected = navigateToTab,
                onLogout      = onLogout          // ← branché ici
            )
        }

        composable(Destination.Notifications.route) {
            NotificationsScreen(onTabSelected = navigateToTab)
        }
    }
}