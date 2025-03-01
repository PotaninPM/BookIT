package com.prod.finaldraftforprod.presentation.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.prod.finaldraftforprod.presentation.screens.home.HomeNavigation
import com.prod.finaldraftforprod.presentation.screens.settings.SettingsScreen
import com.prod.finaldraftforprod.presentation.screens.welcome.welcome.WelcomeScreen
import com.prod.finaldraftforprod.presentation.screens.RootNavDestinations
import com.prod.finaldraftforprod.presentation.screens.booking.BookingScreen

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
//        startDestination = RootNavDestinations.Welcome,
        startDestination = RootNavDestinations.Booking,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

        composable<RootNavDestinations.Home> {
            HomeNavigation(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.Welcome> {
            WelcomeScreen(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.Settings> {
            SettingsScreen(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.Booking> {
            BookingScreen()
        }
    }
}