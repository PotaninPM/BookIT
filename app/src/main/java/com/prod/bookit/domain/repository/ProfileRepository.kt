package com.prod.bookit.domain.repository

import com.prod.bookit.data.remote.dto.booking.BookingCheckDto
import com.prod.bookit.data.remote.dto.coworkings.AvailableSlotsResponse
import com.prod.bookit.data.remote.dto.coworkings.TimeSlot
import com.prod.bookit.data.remote.dto.profile.BookingWithOptionsDto
import com.prod.bookit.domain.model.ProfileBookingModel
import com.prod.bookit.domain.model.UserProfile

interface ProfileRepository {
    suspend fun getProfile(): UserProfile

    suspend fun getBookings(): List<ProfileBookingModel>

    suspend fun deleteBooking(id: String): List<ProfileBookingModel>

    suspend fun checkBooking(id: String): BookingCheckDto

    suspend fun rescheduleBooking(
        bookingId: String,
        newTimeFrom: String,
        newTimeUntil: String
    ): BookingWithOptionsDto
}