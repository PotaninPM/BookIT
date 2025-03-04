package com.prod.bookit.presentation.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.prod.bookit.data.remote.dto.booking.BookingCheckDto
import com.prod.bookit.data.remote.dto.profile.BookingWithOptionsDto
import com.prod.bookit.domain.model.ProfileBookingModel
import com.prod.bookit.domain.model.UserProfile
import com.prod.bookit.domain.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

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

    private val _bookingInfo = MutableStateFlow<BookingCheckDto?>(null)
    val bookingInfo: StateFlow<BookingCheckDto?> = _bookingInfo

    fun closeDialog() {
        viewModelScope.launch {
            _bookingInfo.value = null
        }
    }

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

    fun changeUserInfo(userId: String, email: String, fullName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.changeUserInfo(userId, email, fullName)
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
        return try {
            repository.rescheduleBooking(bookingId, newTimeFrom, newTimeUntil)
        } catch (e: Exception) {
            BookingWithOptionsDto(
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
}