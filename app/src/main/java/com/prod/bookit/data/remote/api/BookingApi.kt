package com.prod.bookit.data.remote.api

import com.prod.bookit.data.remote.dto.booking.BookRequestDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.Path

interface BookingApi {

    @POST("bookings")
    suspend fun book(@Body body: BookRequestDto): Response<Unit>

    @DELETE("bookings/{booking_id}")
    suspend fun cancelBooking(@Path("booking_id") id: String)
}