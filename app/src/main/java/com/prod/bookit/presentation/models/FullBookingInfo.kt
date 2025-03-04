package com.prod.bookit.presentation.models

import java.time.LocalDate
import java.time.LocalTime

data class FullBookingInfo(
    val id: String = "",
    val userId: String = "",
    val position: Int = 1,
    val status: String = "",
    val date: LocalDate = LocalDate.now(),
    val timeFrom: LocalTime = LocalTime.now(),
    val timeUntil: LocalTime = LocalTime.now(),
    val photoUrl: String? = "",
    val name: String = "",
    val email: String = ""
)