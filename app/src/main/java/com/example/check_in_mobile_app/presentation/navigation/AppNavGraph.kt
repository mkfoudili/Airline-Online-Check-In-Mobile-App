package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.auth.LoginScreen
import com.example.check_in_mobile_app.presentation.auth.RegisterScreen
import com.example.check_in_mobile_app.presentation.boarding.BoardingScreen
import com.example.check_in_mobile_app.presentation.booking.AllBookingsScreen
import com.example.check_in_mobile_app.presentation.booking.BookingScreen
<<<<<<< HEAD
import com.example.check_in_mobile_app.presentation.checkin.SeatSelection
import com.example.check_in_mobile_app.presentation.checkin.specialRequest
=======
import com.example.check_in_mobile_app.presentation.booking.FlightDetailsScreen
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageScreen
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageViewModel
import com.example.check_in_mobile_app.presentation.checkin.confirmation.ConfirmationScreen
import com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview.CheckingDetailsReviewScreen
import com.example.check_in_mobile_app.presentation.checkin.passportscan.PassportScanScreen
>>>>>>> b46eaf59e471c2ba0dbf3ebf8609cf97e9ae06c7
import com.example.check_in_mobile_app.presentation.components.TabItem
import com.example.check_in_mobile_app.presentation.home.HomeScreen
import com.example.check_in_mobile_app.presentation.welcome.SplashScreen
import com.example.check_in_mobile_app.presentation.welcome.WelcomeScreen
import com.example.data.repository.BookingRepositoryImpl
import kotlinx.coroutines.delay

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
                popUpTo(Destination.Home.route) {
                    saveState = true
                    inclusive = false
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = Destination.Welcome.route,
        // Tab-level screens: instant switch (no animation) — feels most natural for bottom nav
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(route = Destination.Splash.route) {
            SplashScreen()

            LaunchedEffect(Unit) {
                delay(5_000L)
                navController.navigate(Destination.Welcome.route) {
                    popUpTo(Destination.Splash.route) { inclusive = true }
                }
            }
        }
        composable(route = Destination.Selection.route) {
            SeatSelection(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(route = Destination.preference.route) {
            specialRequest(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(route = Destination.Welcome.route) {
            WelcomeScreen(
                onGetStarted = {
                    navController.navigate(Destination.Register.route)
                },
                onSignIn = {
                    navController.navigate(Destination.Login.route)
                }
            )
        }
        composable(route = Destination.Home.route) {
            HomeScreen(
                onTabSelected = navigateToTab,
                onNavigateToBoardingScreen = {
                    navController.navigate(Destination.Boarding.route)
                }
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
                },
                onBoarding = {
                    navController.navigate(Destination.Boarding.route)
                }
            )
        }

        // Deeper screen: slide in/out horizontally
        composable(
            route = Destination.AllBookings.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            AllBookingsScreen(
                onNavigateBack = { navController.popBackStack() },
                onBoarding = {
                    navController.navigate(Destination.Boarding.route)
                }
            )
        }
        
        composable(route = Destination.FlightDetails.route) { backStackEntry ->
            val bookingRef = backStackEntry.arguments?.getString("bookingRef") ?: ""
            // Simple mock extraction for UI logic
            val booking = BookingRepositoryImpl()
                .getUpcomingBookings()
                .find { it.bookingRef == bookingRef } 
                ?: BookingRepositoryImpl().getUpcomingBookings().first()

            FlightDetailsScreen(
                booking = booking,
                onBack = { navController.popBackStack() },
                onStartCheckIn = {
                    navController.navigate(Destination.PassportScan.route)
                }
            )
        }
        composable(route = Destination.Boarding.route) {
            BoardingScreen(
                onBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = Destination.PassportScan.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            PassportScanScreen(
                onBack = { navController.popBackStack() },
                onContinue = {
                    navController.navigate(Destination.CheckingDetailsReview.route)
                }
            )
        }
        composable(route = Destination.Baggage.route) {
            BaggageScreen(
                viewModel = viewModel<BaggageViewModel>(),
                onBackClick = { navController.popBackStack() },
                onContinueClick = {
                    navController.navigate(Destination.Boarding.route)
                }
            )
        }
        composable(route = Destination.Confirmation.route) {
            ConfirmationScreen (
                onNavigateToHomeScreen = {
                    navController.navigate(Destination.Home.route) {
                        popUpTo(Destination.Home.route) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = Destination.CheckingDetailsReview.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            CheckingDetailsReviewScreen(
                onBack = { navController.popBackStack() },
                onContinue = { /* Step 3: Seat Selection — coming soon */ }
            )
        }
        composable(Destination.Login.route) {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoginSuccess = { navController.navigate(Destination.Home.route) },
                onNavigateToRegister = { navController.navigate(Destination.Register.route) }
            )
        }
        composable(Destination.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { navController.navigate(Destination.Home.route) },
                onNavigateToLogin = { navController.navigate(Destination.Login.route) }
            )
        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AppNavGraphPreview() {
    AppNavGraph()
}

