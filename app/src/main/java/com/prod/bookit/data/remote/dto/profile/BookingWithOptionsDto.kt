package com.prod.bookit.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class BookingWithOptionsDto(
    val id: String,
    @SerializedName("user_id") val userId: String,
    @SerializedName("spot_id") val spotId: String,
    @SerializedName("time_from") val timeFrom: String,
    @SerializedName("time_until") val timeUntil: String,
    val status: String,
    val options: List<String>
)

