package com.example.check_in_mobile_app.presentation.navigation


import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.checkin.SeatSelection
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageScreen
import com.example.check_in_mobile_app.presentation.checkin.baggage.BaggageViewModel
import com.example.check_in_mobile_app.presentation.checkin.confirmation.ConfirmationScreen
import com.example.check_in_mobile_app.presentation.checkin.checkingdetailsreview.CheckingDetailsReviewScreen
import com.example.check_in_mobile_app.presentation.checkin.passportscan.PassportScanScreen
import com.example.check_in_mobile_app.presentation.checkin.specialRequest

@Composable
fun CheckInNavGraph(
    bookingRef: String,
    onCheckInComplete: () -> Unit
) {
    val navController = rememberNavController()

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
                onBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Destination.CheckingDetailsReview.route) }
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
                onContinue = { navController.navigate(Destination.Selection.route) }
            )
        }
        composable(Destination.Selection.route) {
            SeatSelection(
                onNavigateBack = { navController.popBackStack() },
                onContinue = { navController.navigate(Destination.Baggage.route) }
            )
        }
        composable(Destination.Baggage.route) {
            BaggageScreen(
                viewModel = viewModel<BaggageViewModel>(),
                onBackClick = { navController.popBackStack() },
                onContinueClick = { navController.navigate(Destination.preference.route) }
            )
        }
        composable(Destination.preference.route) {
            specialRequest(
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