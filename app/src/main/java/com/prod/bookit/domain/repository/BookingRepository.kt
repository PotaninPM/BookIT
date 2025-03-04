package com.prod.bookit.domain.repository

import com.prod.bookit.domain.model.BookObjectUIData
import com.prod.bookit.presentation.models.BookingStatus
import com.prod.bookit.presentation.models.FullBookingInfo
import java.time.LocalDate
import java.time.LocalTime

interface BookingRepository {

    suspend fun book(
        spotId: String, timeFrom: LocalTime,
        timeUntil: LocalTime, date: LocalDate
    ): BookingStatus

    suspend fun getSpotsForCoworking(
        coworkingId: String, timeFrom: LocalTime,
        timeUntil: LocalTime, date: LocalDate
    ): List<BookObjectUIData>

    suspend fun getCurrentBookingForSpot(spotId: String): FullBookingInfo

    suspend fun getAllBokings(page: Int, count: Int): List<FullBookingInfo>

    suspend fun cancelBooking(bookingId: String)
}