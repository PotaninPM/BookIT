package com.prod.bookit.data.remote.dto.booking

import com.google.gson.annotations.SerializedName

data class BookingCheckDto(
    @SerializedName("time_from")
    val timeFrom: String,

    @SerializedName("time_until")
    val timeUntil: String,

    val id: String,

    val user: BookingUserDto,

    val spot: BookingSpotDto,

    val status: String,

    val options: List<String>
)

data class BookingUserDto(
    val id: String,

    @SerializedName("avatar_url")
    val avatarUrl: String,

    @SerializedName("full_name")
    val fullName: String,

    val email: String
)

data class BookingSpotDto(
    val id: String,
    val name: String
)