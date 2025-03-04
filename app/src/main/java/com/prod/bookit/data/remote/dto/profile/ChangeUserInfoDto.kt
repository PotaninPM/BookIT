package com.prod.bookit.data.remote.dto.profile

import com.google.gson.annotations.SerializedName

data class ChangeUserInfoDto(
    val email: String,
    @SerializedName("full_name") val fullName: String
)
