package com.example.check_in_mobile_app.presentation.navigation

import android.content.Intent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.check_in_mobile_app.presentation.auth.AuthViewModel
import com.example.check_in_mobile_app.presentation.auth.LoginScreen
import com.example.check_in_mobile_app.presentation.auth.RegisterScreen
import com.example.check_in_mobile_app.presentation.auth.welcome.SplashScreen
import com.example.check_in_mobile_app.presentation.auth.welcome.WelcomeScreen
import com.example.check_in_mobile_app.presentation.main.MainActivity
import kotlinx.coroutines.delay

@Composable
fun LoginNavGraph(
    viewModel: AuthViewModel,
    onLoginSuccess: () -> Unit) {
    val navController = rememberNavController()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState(initial = false)

    NavHost(
        navController = navController,
        startDestination = Destination.Splash.route,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable(Destination.Splash.route) {
            SplashScreen()
            LaunchedEffect(isLoggedIn) {
                delay(5_000L)
                if (isLoggedIn) {
                    onLoginSuccess()
                } else {
                    navController.navigate(Destination.Welcome.route) {
                        popUpTo(Destination.Splash.route) { inclusive = true }
                    }
                }
            }
        }
        composable(Destination.Welcome.route) {
            WelcomeScreen(
                onGetStarted = { navController.navigate(Destination.Register.route) },
                onSignIn = { navController.navigate(Destination.Login.route) }
            )
        }
        composable(Destination.Login.route) {
            LoginScreen(
                onNavigateBack = { navController.popBackStack() },
                onLoginSuccess = { onLoginSuccess() },
//                onNavigateToHomeScreen = {  onLoginSuccess()
//
//                } ,
                onNavigateToRegister = { navController.navigate(Destination.Register.route) }
            )
        }
        composable(Destination.Register.route) {
            RegisterScreen(
                onNavigateBack = { navController.popBackStack() },
                onRegisterSuccess = { onLoginSuccess() },
                onNavigateToLogin = { navController.navigate(Destination.Login.route) }
            )
        }
    }
}