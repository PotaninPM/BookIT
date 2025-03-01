package com.prod.finaldraftforprod.presentation.screens

import kotlinx.serialization.Serializable

sealed class RootNavDestinations {

    @Serializable
    data object Welcome : RootNavDestinations()

    @Serializable
    data object Home : RootNavDestinations()

    @Serializable
    data object Settings : RootNavDestinations()

    @Serializable
    data object Booking : RootNavDestinations()
}