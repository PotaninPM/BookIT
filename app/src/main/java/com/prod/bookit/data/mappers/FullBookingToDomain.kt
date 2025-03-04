package com.prod.bookit.data.mappers

import com.prod.bookit.data.remote.dto.booking.FullBookingDto
import com.prod.bookit.presentation.models.FullBookingInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

fun FullBookingDto.toDomain(): FullBookingInfo = FullBookingInfo(
    id = id,
    userId = user.id,
    position = spot.name.drop(1).toInt(),
    date = LocalDateTime.parse(time_from, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalDate(),
    timeFrom = LocalDateTime.parse(time_from, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalTime(),
    timeUntil = LocalDateTime.parse(time_until, DateTimeFormatter.ISO_OFFSET_DATE_TIME).toLocalTime(),
    photoUrl = user.avatar_url,
    name = user.full_name,
    status = status,
    email = user.email
)