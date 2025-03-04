package com.prod.bookit.data.repository

import com.prod.bookit.data.mappers.toDomain
import com.prod.bookit.data.remote.api.ProfileApi
import com.prod.bookit.data.remote.dto.booking.BookingCheckDto
import com.prod.bookit.data.remote.dto.profile.BookingWithOptionsDto
import com.prod.bookit.data.remote.dto.profile.ChangeUserInfoDto
import com.prod.bookit.data.remote.dto.profile.RescheduleBookingRequest
import com.prod.bookit.domain.model.ProfileBookingModel
import com.prod.bookit.domain.model.UserProfile
import com.prod.bookit.domain.repository.ProfileRepository

class ProfileRepositoryImpl(
    private val api: ProfileApi
): ProfileRepository {
    override suspend fun getProfile(): UserProfile {

        try {
            val profile = api.getProfile()
            return profile.toDomain()
        } catch (e: Exception) {
            e.printStackTrace()
            return UserProfile(
                "1",
                 "Неизвестно",
                 "",
                false,
                "Неизвестно"
            )
        }

    }

    override suspend fun getBookings(): List<ProfileBookingModel> {
        return try {
            api.getBookings().map { it.toDomain() }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun deleteBooking(id: String): List<ProfileBookingModel> {
        return try {
            api.cancelBooking(id)
            getBookings()
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    override suspend fun checkBooking(id: String): BookingCheckDto {
        return api.getBooking(id)
    }

    override suspend fun rescheduleBooking(
        bookingId: String,
        newTimeFrom: String,
        newTimeUntil: String
    ): BookingWithOptionsDto {
        return try {
            val request = RescheduleBookingRequest(
                timeFrom = newTimeFrom,
                timeUntil = newTimeUntil
            )
            api.rescheduleBooking(bookingId, request)
        } catch (e: Exception) {
            e.printStackTrace()
            return BookingWithOptionsDto(
                "-1",
                "1",
                "1",
                "1",
                "1",
                "1",
                listOf()
            )
        }
    }

    override suspend fun changeUserInfo(userId: String, email: String, fullName: String) {
        try {
            api.changeUserInfo(userId, ChangeUserInfoDto(email, fullName))
        } catch (e: Exception) {

        }
    }
}