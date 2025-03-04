package com.prod.bookit.data.repository

import com.prod.bookit.common.AppDispatchers
import com.prod.bookit.data.mappers.toDomain
import com.prod.bookit.data.remote.api.BookingApi
import com.prod.bookit.data.remote.api.CoworkingsApi
import com.prod.bookit.data.remote.dto.booking.BookRequestDto
import com.prod.bookit.data.remote.dto.booking.FullBookingDto
import com.prod.bookit.data.remote.dto.coworkings.SpotDto
import com.prod.bookit.domain.model.BookObjectUIData
import com.prod.bookit.domain.repository.BookingRepository
import com.prod.bookit.presentation.models.BookingStatus
import com.prod.bookit.presentation.models.FullBookingInfo
import com.prod.bookit.presentation.util.serializeDateAndTime
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate
import java.time.LocalTime

class BookingRepositoryImpl(
    private val api: BookingApi,
    private val coworkingsApi: CoworkingsApi,
    private val dispatchers: AppDispatchers
) : BookingRepository {

    override suspend fun book(
        spotId: String,
        timeFrom: LocalTime,
        timeUntil: LocalTime,
        date: LocalDate
    ): BookingStatus = withContext(dispatchers.io) {
        try {
            val resp = api.book(BookRequestDto.create(spotId, timeFrom, timeUntil, date))

            if (resp.isSuccessful) return@withContext BookingStatus.Success

            val body = resp.errorBody()?.string() ?: return@withContext BookingStatus.Error(null)

            val spot = Json.parseToJsonElement(body).jsonObject["spot"]?.jsonObject ?: return@withContext BookingStatus.Error(null)

            return@withContext BookingStatus.Error(BookObjectUIData(
                id = spot["id"]!!.jsonPrimitive.content,
                position = spot["name"]!!.jsonPrimitive.content.drop(1).toInt(),
                avalibleToBook = true
            ))
        } catch (_: Exception) {
            BookingStatus.Error(null)
        }
    }

    override suspend fun getSpotsForCoworking(
        coworkingId: String,
        timeFrom: LocalTime,
        timeUntil: LocalTime,
        date: LocalDate
    ): List<BookObjectUIData> = withContext(dispatchers.io) {
        coworkingsApi.getSpots(
            coworkingId = coworkingId,
            timeFrom = serializeDateAndTime(timeFrom, date),
            timeUntil = serializeDateAndTime(timeUntil, date)
        ).map(SpotDto::toDomain)
    }

    override suspend fun getCurrentBookingForSpot(spotId: String): FullBookingInfo = withContext(dispatchers.io) {
        coworkingsApi.getCurrentBookingForSpot(spotId).toDomain()
    }

    override suspend fun getAllBokings(page: Int, count: Int): List<FullBookingInfo> = withContext(dispatchers.io) {
        coworkingsApi.getAllBookings(count = count, page = page).map(FullBookingDto::toDomain)
    }

    override suspend fun cancelBooking(bookingId: String) {
        api.cancelBooking(bookingId)
    }
}