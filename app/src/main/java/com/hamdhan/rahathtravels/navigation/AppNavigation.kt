package com.hamdhan.rahathtravels.navigation

import androidx.compose.runtime.*
import com.hamdhan.rahathtravels.authentication.ForgotPasswordScreen
import com.hamdhan.rahathtravels.authentication.LoginScreen
import com.hamdhan.rahathtravels.authentication.RegisterScreen
import com.hamdhan.rahathtravels.authentication.SplashScreen
import com.hamdhan.rahathtravels.dashboard.BookingScreen
import com.hamdhan.rahathtravels.dashboard.HomeScreen
import com.hamdhan.rahathtravels.dashboard.ProfileScreen

sealed class Screen {
    object Splash : Screen()
    object Login : Screen()
    object Register : Screen()
    object ForgotPassword : Screen()
    object Home : Screen()
    object Booking : Screen()
    object Profile : Screen()
}

@Composable
fun AppNavigation() {

    var currentScreen by remember { mutableStateOf<Screen>(Screen.Splash) }

    when (currentScreen) {

        is Screen.Splash -> {
            SplashScreen(
                onSplashFinished = { currentScreen = Screen.Login }
            )
        }

        is Screen.Login -> {
            LoginScreen(
                onLoginSuccess = { currentScreen = Screen.Home },
                onNavigateToRegister = { currentScreen = Screen.Register },
                onNavigateToForgotPassword = { currentScreen = Screen.ForgotPassword }
            )
        }

        is Screen.Register -> {
            RegisterScreen(
                onRegisterSuccess = { currentScreen = Screen.Login },
                onNavigateToLogin = { currentScreen = Screen.Login }
            )
        }

        is Screen.ForgotPassword -> {
            ForgotPasswordScreen(
                onNavigateBack = { currentScreen = Screen.Login }
            )
        }

        is Screen.Home -> {
            HomeScreen(
                onNavigateToBooking = { currentScreen = Screen.Booking },
                onNavigateToProfile = { currentScreen = Screen.Profile }
            )
        }

        is Screen.Booking -> {
            BookingScreen(
                onNavigateBack = { currentScreen = Screen.Home }
            )
        }

        is Screen.Profile -> {
            ProfileScreen(
                onLogout = { currentScreen = Screen.Login },
                onNavigateBack = { currentScreen = Screen.Home }
            )
        }
    }
}