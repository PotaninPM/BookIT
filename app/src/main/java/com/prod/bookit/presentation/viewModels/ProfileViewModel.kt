package com.prod.bookit.presentation.viewModels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prod.bookit.data.remote.dto.coworkings.AvailableSlotsResponse
import com.prod.bookit.data.remote.dto.coworkings.TimeSlot
import com.prod.bookit.data.remote.dto.profile.BookingWithOptionsDto
import com.prod.bookit.domain.model.ProfileBookingModel
import com.prod.bookit.domain.model.UserProfile
import com.prod.bookit.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val repository: ProfileRepository
): ViewModel() {
    private val _profile = MutableStateFlow<UserProfile?>(null)
    val profile: StateFlow<UserProfile?> = _profile

    private val _bookings = MutableStateFlow<List<ProfileBookingModel>>(emptyList())
    val bookings: StateFlow<List<ProfileBookingModel>> = _bookings

    private val _isLoaded = MutableStateFlow(false)
    val isLoaded: StateFlow<Boolean> = _isLoaded

    init {
        loadProfile()
    }

    private val _bookingInfo = MutableStateFlow<BookingWithOptionsDto?>(null)
    val bookingInfo: StateFlow<BookingWithOptionsDto?> = _bookingInfo

    fun loadProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoaded.value = false
            try {
                repository.getProfile().let {
                    _profile.value = it
                    _isLoaded.value = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteBooking(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.deleteBooking(id)
                loadBookings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun loadBookings() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _bookings.value = repository.getBookings()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun checkBooking(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _bookingInfo.value = repository.checkBooking(id)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun rescheduleBooking(
        bookingId: String,
        newTimeFrom: String,
        newTimeUntil: String
    ): BookingWithOptionsDto {
        return repository.rescheduleBooking(bookingId, newTimeFrom, newTimeUntil)
    }
}