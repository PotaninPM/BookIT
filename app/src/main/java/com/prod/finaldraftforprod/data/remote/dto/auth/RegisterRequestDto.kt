package com.prod.finaldraftforprod.data.remote.dto.auth

import kotlinx.serialization.Serializable

@Serializable
data class RegisterRequestDto(
    val email: String,
    val password: String
)
