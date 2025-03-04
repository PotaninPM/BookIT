package com.prod.bookit.data.remote.api

import com.prod.bookit.data.remote.dto.booking.BookRequestDto
import com.prod.bookit.data.remote.dto.booking.FullBookingDto
import com.prod.bookit.data.remote.dto.coworkings.AvailableSlotsResponse
import com.prod.bookit.data.remote.dto.coworkings.CoworkingDetailDto
import com.prod.bookit.data.remote.dto.coworkings.CoworkingSummaryDto
import com.prod.bookit.data.remote.dto.coworkings.SpotDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface CoworkingsApi {

    @GET("coworkings")
    suspend fun getAllCoworkings(): List<CoworkingSummaryDto>

    @GET("coworkings/{id}")
    suspend fun getCoworkingById(
        @Path("id") id: String
    ): CoworkingDetailDto

    @GET("coworkings/{coworking_id}/spots")
    suspend fun getSpots(
        @Path("coworking_id") coworkingId: String,
        @Query("time_from") timeFrom: String,
        @Query("time_until") timeUntil: String
    ): List<SpotDto>

//    @POST("bookings")
//    suspend fun createBooking(
//        @Body request: BookRequestDto
//    ): BookingWithOptionsDto
//
//    @GET("users/me/bookings")
//    suspend fun getMyBookings(): List<UserBookingDto>
//
//    @GET("bookings/{booking_id}/available-times")
//    suspend fun getAvailableTimes(
//        @Path("booking_id") bookingId: String
//    ): AvailableSlotsRespons
//
//    @GET("bookings/{booking_id}")
//    suspend fun getBookingDetails(
//        @Path("booking_id") bookingId: String
//    ): BookingWithOptionsDto
//
    @GET("spots/{spot_id}/current-booking")
    suspend fun getCurrentBookingForSpot(
        @Path("spot_id") spotId: String
    ): FullBookingDto
//
    @GET("bookings")
    suspend fun getAllBookings(
        @Query("count") count: Int,
        @Query("page") page: Int
    ): List<FullBookingDto>


//
//    @POST("options")
//    suspend fun createOption(
//        @Body request: CreateOptionRequest
//    ): OptionDto
//
//    @DELETE("options/{option_id}")
//    suspend fun deleteOption(
//        @Path("option_id") optionId: String
//    ): Unit
//
//    @POST("bookings/{booking_id}/options")
//    suspend fun addOptionToBooking(
//        @Path("booking_id") bookingId: String,
//        @Body request: AddOptionRequest
//    ): BookingWithOptionsDto
}