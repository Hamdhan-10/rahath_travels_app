package com.hamdhan.rahathtravels.dashboard

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hamdhan.rahathtravels.data.model.Booking
import com.hamdhan.rahathtravels.data.room.AppDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class BookingViewModel(application: Application) : AndroidViewModel(application) {

    // Room database
    private val bookingDao = AppDatabase.getDatabase(application).bookingDao()

    // Firebase
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    // State to track what's happening
    private val _bookingState = MutableStateFlow<BookingState>(BookingState.Idle)
    val bookingState: StateFlow<BookingState> = _bookingState

    // All bookings for current user
    val userBookings = auth.currentUser?.uid?.let { uid ->
        bookingDao.getBookingsByUser(uid)
    }

    fun submitBooking(
        fullName: String,
        email: String,
        phone: String,
        packageType: String,
        numberOfPersons: String,
        preferredDate: String,
        specialRequests: String
    ) {
        // Validation
        if (fullName.isBlank() || email.isBlank() ||
            phone.isBlank() || packageType.isBlank() ||
            numberOfPersons.isBlank() || preferredDate.isBlank()
        ) {
            _bookingState.value = BookingState.Error("Please fill in all required fields")
            return
        }

        _bookingState.value = BookingState.Loading

        val userId = auth.currentUser?.uid ?: ""
        val currentDate = SimpleDateFormat(
            "dd/MM/yyyy HH:mm",
            Locale.getDefault()
        ).format(Date())

        val booking = Booking(
            userId = userId,
            fullName = fullName,
            email = email,
            phone = phone,
            packageType = packageType,
            numberOfPersons = numberOfPersons,
            preferredDate = preferredDate,
            specialRequests = specialRequests,
            status = "Pending",
            bookingDate = currentDate
        )

        viewModelScope.launch {
            try {
                // Save to Room (local database)
                bookingDao.insertBooking(booking)

                // Save to Firestore (cloud database)
                val bookingMap = hashMapOf(
                    "userId" to userId,
                    "fullName" to fullName,
                    "email" to email,
                    "phone" to phone,
                    "packageType" to packageType,
                    "numberOfPersons" to numberOfPersons,
                    "preferredDate" to preferredDate,
                    "specialRequests" to specialRequests,
                    "status" to "Pending",
                    "bookingDate" to currentDate
                )

                firestore.collection("bookings")
                    .add(bookingMap)

                _bookingState.value = BookingState.Success

            } catch (e: Exception) {
                _bookingState.value = BookingState.Error(
                    e.message ?: "Booking failed. Please try again."
                )
            }
        }
    }

    fun resetState() {
        _bookingState.value = BookingState.Idle
    }

    fun deleteBooking(booking: Booking) {
        viewModelScope.launch {
            bookingDao.deleteBooking(booking)
        }
    }
}

// All possible states for booking
sealed class BookingState {
    object Idle : BookingState()
    object Loading : BookingState()
    object Success : BookingState()
    data class Error(val message: String) : BookingState()
}