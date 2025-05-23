package com.prod.bookit.data.remote.dto.coworkings

import com.google.gson.annotations.SerializedName

data class CoworkingDetailDto(
    val id: String,
    val name: String,
    val description: String,
    val address: String,
    val capacity: Int,
    @SerializedName("opens_at") val opensAt: String,
    @SerializedName("closes_at") val endsAt: String,
    val images: List<String>
)