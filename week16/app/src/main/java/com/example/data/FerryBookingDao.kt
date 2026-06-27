package com.example.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FerryBookingDao {
    @Query("SELECT * FROM ferry_bookings ORDER BY bookingTime DESC")
    fun getAllBookings(): Flow<List<FerryBooking>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: FerryBooking): Long

    @Delete
    suspend fun deleteBooking(booking: FerryBooking)

    @Query("DELETE FROM ferry_bookings")
    suspend fun clearAllBookings()
}
