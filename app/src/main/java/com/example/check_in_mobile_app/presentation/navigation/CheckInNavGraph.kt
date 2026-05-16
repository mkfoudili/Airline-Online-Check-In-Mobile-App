package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.checkin.CheckInViewModel
import com.example.check_in_mobile_app.presentation.checkin.SeatSelection
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageScreen
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageViewModel
import com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview.CheckingDetailsReviewScreen
import com.example.check_in_mobile_app.presentation.checkin.confirmation.ConfirmationScreen
import com.example.check_in_mobile_app.presentation.checkin.passportscan.PassportScanScreen
import com.example.check_in_mobile_app.presentation.checkin.specialRequest

@Composable
fun CheckInNavGraph(
    bookingRef: String,
    onBackFromFirstStep: () -> Unit,
    onCheckInComplete: () -> Unit,
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val booking by viewModel.booking.collectAsState()

    LaunchedEffect(bookingRef) {
        viewModel.loadBooking(bookingRef)
    }

    NavHost(
        navController = navController,
        startDestination = Destination.PassportScan.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(
            route = Destination.PassportScan.route,
            enterTransition = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            PassportScanScreen(
                onBack = onBackFromFirstStep,
                onContinue = { navController.navigate(Destination.CheckingDetailsReview.route) },
                viewModel = hiltViewModel()
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
                onContinue = { navController.navigate(Destination.Selection.route) },
                viewModel = hiltViewModel()
            )
        }
        composable(Destination.Selection.route) {
            val flightId = booking?.flight?.flightId ?: ""
            val passengerId = booking?.passengers?.firstOrNull()?.passengerId ?: ""
            
            SeatSelection(
                flightId = flightId,
                passengerId = passengerId,
                onNavigateBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Destination.Baggage.route) }
            )
        }
        composable(Destination.Baggage.route) {
            BaggageScreen(
                viewModel = hiltViewModel<BaggageViewModel>(),
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(Destination.preference.route) }
            )
        }
        composable(Destination.preference.route) {
            val passengerId = booking?.passengers?.firstOrNull()?.passengerId ?: ""
            specialRequest(
                passengerId = passengerId,
                onNavigateBack = { navController.popBackStack() },
                onFinishCheckIn = { navController.navigate(Destination.Confirmation.route) }
            )
        }
        composable(Destination.Confirmation.route) {
            ConfirmationScreen(
                onNavigateToHomeScreen = { onCheckInComplete() }
            )
        }
    }
}