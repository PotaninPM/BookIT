package com.prod.bookit.data.remote.dto.coworkings

import com.google.gson.annotations.SerializedName

data class CoworkingSummaryDto(
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("address") val address: String,
    @SerializedName("opens_at") val openingTime: String,
    @SerializedName("closes_at") val closingTime: String
)
