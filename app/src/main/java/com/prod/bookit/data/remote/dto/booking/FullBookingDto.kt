package com.prod.bookit.data.remote.dto.booking

data class FullBookingDto(
    val id: String,
    val user: UserDto,
    val spot: SpotInfoAdminDto,
    val time_from: String,
    val status: String,
    val time_until: String,
    val options: List<String>
)

data class UserDto(
    val id: String,
    val avatar_url: String?,
    val full_name: String,
    val email: String
)

data class SpotInfoAdminDto(
    val id: String,
    val name: String
)

