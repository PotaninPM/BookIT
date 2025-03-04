package com.prod.bookit.presentation.screens

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.prod.bookit.domain.repository.AuthRepository
import com.prod.bookit.presentation.models.Coworking
import com.prod.bookit.presentation.screens.settings.SettingsScreen
import com.prod.bookit.presentation.screens.welcome.welcome.WelcomeScreen
import com.prod.bookit.presentation.screens.booking.BookingScreen
import com.prod.bookit.presentation.screens.coworkings.CoworkingsScreen
import com.prod.bookit.presentation.screens.profile.ProfileScreen
import com.prod.bookit.presentation.screens.qrScan.QrScanner
import org.koin.compose.getKoin

@Composable
fun RootNavigation() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = getKoin().get<AuthRepository>().getToken()?.let {
           //RootNavDestinations.QrCode
            RootNavDestinations.Booking(coworkingId = Coworking.coworkings[0].id)
        } ?: RootNavDestinations.Welcome,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {

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
            val route = it.toRoute<RootNavDestinations.Booking>()

            BookingScreen(
                rootNavController = rootNavController,
                coworking = Coworking.coworkings.first { it.id == route.coworkingId }
            )
        }

        composable<RootNavDestinations.Coworkings> {
            CoworkingsScreen(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.Profile> {
            ProfileScreen(
                rootNavController = rootNavController
            )
        }

        composable<RootNavDestinations.QrCode> {
            QrScanner(
                rootNavController = rootNavController
            )
        }
    }
}