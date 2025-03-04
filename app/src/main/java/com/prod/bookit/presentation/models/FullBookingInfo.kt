package com.prod.bookit.presentation.models

import java.time.LocalDate
import java.time.LocalTime

data class FullBookingInfo(
    val id: String = "",
    val position: Int,
    val date: LocalDate,
    val timeFrom: LocalTime,
    val timeUntil: LocalTime,
    val photoUrl: String?,
    val name: String,
    val email: String
)