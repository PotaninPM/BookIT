package com.prod.bookit.data.remote.dto.auth

import com.google.gson.annotations.SerializedName

data class NotificationsRequestDto(
    val token: String,
    @SerializedName("device_type") val deviceType: String = "android"
)
