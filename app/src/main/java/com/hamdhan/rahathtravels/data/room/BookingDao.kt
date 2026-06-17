package com.hamdhan.rahathtravels.data.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.hamdhan.rahathtravels.data.model.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Query("SELECT * FROM bookings WHERE userId = :userId ORDER BY id DESC")
    fun getBookingsByUser(userId: String): Flow<List<Booking>>

    @Query("SELECT * FROM bookings ORDER BY id DESC")
    fun getAllBookings(): Flow<List<Booking>>

    @Update
    suspend fun updateBooking(booking: Booking)

    @Delete
    suspend fun deleteBooking(booking: Booking)

    @Query("DELETE FROM bookings WHERE id = :bookingId")
    suspend fun deleteBookingById(bookingId: Int)
}