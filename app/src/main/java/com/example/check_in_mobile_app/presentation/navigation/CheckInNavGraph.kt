package com.example.check_in_mobile_app.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.*
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.check_in_mobile_app.presentation.checkin.CheckInSessionViewModel
import com.example.check_in_mobile_app.presentation.checkin.CheckInViewModel
import com.example.check_in_mobile_app.presentation.checkin.SeatSelection
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageScreen
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageViewModel
import com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview.CheckingDetailsReviewScreen
import com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview.CheckingDetailsReviewViewModel
import com.example.check_in_mobile_app.presentation.checkin.confirmation.ConfirmationScreen
import com.example.check_in_mobile_app.presentation.checkin.passportscan.PassportScanScreen
import com.example.check_in_mobile_app.presentation.checkin.specialRequest

@Composable
fun CheckInNavGraph(
    bookingRef: String,
    bookingId: String,
    passengerId: String,
    onBackFromFirstStep: () -> Unit,
    onCheckInComplete: () -> Unit,
    viewModel: CheckInViewModel = hiltViewModel()
) {
    val navController = rememberNavController()
    val sessionViewModel: CheckInSessionViewModel = hiltViewModel()
    val sessionState by sessionViewModel.state.collectAsStateWithLifecycle()
    val booking by viewModel.booking.collectAsState()

    LaunchedEffect(bookingRef) {
        viewModel.loadBooking(bookingRef)
    }

    NavHost(
        navController      = navController,
        startDestination   = Destination.PassportScan.route,
        enterTransition    = { EnterTransition.None },
        exitTransition     = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition  = { ExitTransition.None }
    ) {
        composable(
            route              = Destination.PassportScan.route,
            enterTransition    = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition     = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition  = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            PassportScanScreen(
                onBack           = onBackFromFirstStep,
                onContinue       = { navController.navigate(Destination.CheckingDetailsReview.route) },
                bookingId        = bookingId,
                sessionViewModel = sessionViewModel
            )
        }

        composable(
            route              = Destination.CheckingDetailsReview.route,
            enterTransition    = { slideInHorizontally(initialOffsetX = { it }, animationSpec = tween(300)) },
            exitTransition     = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition  = { slideOutHorizontally(targetOffsetX = { it }, animationSpec = tween(300)) }
        ) {
            val verifiedPassenger = sessionState.verifiedPassenger
            if (verifiedPassenger != null) {
                val viewModel: CheckingDetailsReviewViewModel = hiltViewModel()
                LaunchedEffect(verifiedPassenger) {
                    viewModel.setPassenger(verifiedPassenger)
                }
                CheckingDetailsReviewScreen(
                    onBack     = { navController.popBackStack() },
                    onContinue = { navController.navigate(Destination.Selection.route) },
                    viewModel  = viewModel
                )
            }
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
                viewModel       = hiltViewModel<BaggageViewModel>(),
                onBackClick     = { navController.popBackStack() },
                onContinueClick = {
                    val pid = sessionState.verifiedPassenger?.passengerId ?: passengerId
                    navController.navigate(Destination.preference.routeWithArg(pid))
                }
            )
        }
        composable(
            route = Destination.preference.route,
            arguments = listOf(navArgument("passengerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("passengerId") ?: (sessionState.verifiedPassenger?.passengerId ?: "")
            specialRequest(
                passengerId = pid,
                onNavigateBack = { navController.popBackStack() },
                onFinishCheckIn = {
                    navController.navigate(Destination.Confirmation.routeWithArg(pid))
                }
            )
        }

        composable(
            route     = Destination.Confirmation.route,
            arguments = listOf(navArgument("passengerId") { type = NavType.StringType })
        ) { backStackEntry ->
            val pid = backStackEntry.arguments?.getString("passengerId") ?: passengerId
            ConfirmationScreen(
                passengerId            = pid,
                onNavigateToHomeScreen = { onCheckInComplete() }
            )
        }
    }
}