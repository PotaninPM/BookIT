package com.prod.bookit.presentation.screens

import kotlinx.serialization.Serializable

sealed class RootNavDestinations {

    @Serializable
    data object Welcome : RootNavDestinations()

    @Serializable
    data object Settings : RootNavDestinations()

    @Serializable
    data class Booking(val coworkingId: String) : RootNavDestinations()

    @Serializable
    data object Profile : RootNavDestinations()

    @Serializable
    data object Coworkings : RootNavDestinations()

    @Serializable
    data object QrCode : RootNavDestinations()
}