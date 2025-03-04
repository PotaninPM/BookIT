package com.prod.bookit.presentation.viewModels

import androidx.lifecycle.ViewModel
import com.prod.bookit.domain.repository.BookingRepository
import com.prod.bookit.presentation.models.BookingData
import com.prod.bookit.presentation.models.BookingStatus
import com.prod.bookit.presentation.models.FullBookingInfo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDate
import java.time.LocalTime

class BookingViewModel(
    private val bookingRepository: BookingRepository
): ViewModel() {


    fun book(bookingData: BookingData): Flow<BookingStatus> = flow {
        emit(BookingStatus.Loading)

        val result = bookingRepository.book(
            spotId = bookingData.spotId,
            timeFrom = bookingData.startTime,
            timeUntil = bookingData.endTime,
            date = bookingData.date
        )

        emit(result)
    }

    suspend fun getSpotsForCoworking(
        coworkingId: String, timeFrom: LocalTime,
        timeUntil: LocalTime, date: LocalDate
    ) = bookingRepository.getSpotsForCoworking(coworkingId, timeFrom, timeUntil, date)

    suspend fun getCurrentBookingForSpot(spotId: String) =
        bookingRepository.getCurrentBookingForSpot(spotId)

    suspend fun getAllCoworkings(page: Int, count: Int): List<FullBookingInfo> =
        bookingRepository.getAllBokings(page = page, count = count)

    suspend fun cancelBooking(bookingId: String) {
        bookingRepository.cancelBooking(bookingId)
    }
}