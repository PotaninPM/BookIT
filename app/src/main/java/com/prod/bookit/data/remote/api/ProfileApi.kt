package com.prod.bookit.data.remote.api

import com.prod.bookit.data.remote.dto.booking.BookingCheckDto
import com.prod.bookit.data.remote.dto.profile.BookingWithOptionsDto
import com.prod.bookit.data.remote.dto.profile.ProfileBookingDto
import com.prod.bookit.data.remote.dto.profile.RescheduleBookingRequest
import com.prod.bookit.data.remote.dto.profile.UserProfileDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.Path

interface ProfileApi {

    @GET("users/me")
    suspend fun getProfile(): UserProfileDto

    @GET("users/me/bookings")
    suspend fun getBookings(): List<ProfileBookingDto>

    @GET("bookings/{booking_id}")
    suspend fun getBooking(
        @Path("booking_id") bookingId: String
    ): BookingCheckDto

    @PATCH("bookings/{booking_id}")
    suspend fun rescheduleBooking(
        @Path("booking_id") bookingId: String,
        @Body request: RescheduleBookingRequest
    ): BookingWithOptionsDto

    @DELETE("bookings/{booking_id}")
    suspend fun cancelBooking(
        @Path("booking_id") bookingId: String
    ): Unit
}