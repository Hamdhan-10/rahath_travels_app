package com.hamdhan.rahathtravels.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val userId: String = "",
    val fullName: String = "",
    val email: String = "",
    val phone: String = "",
    val packageType: String = "",
    val numberOfPersons: String = "",
    val preferredDate: String = "",
    val specialRequests: String = "",
    val status: String = "Pending",
    val bookingDate: String = ""
)