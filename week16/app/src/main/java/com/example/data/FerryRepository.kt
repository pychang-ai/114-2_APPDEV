package com.example.data

import kotlinx.coroutines.flow.Flow

class FerryRepository(private val ferryBookingDao: FerryBookingDao) {
    val allBookings: Flow<List<FerryBooking>> = ferryBookingDao.getAllBookings()

    suspend fun insert(booking: FerryBooking): Long {
        return ferryBookingDao.insertBooking(booking)
    }

    suspend fun delete(booking: FerryBooking) {
        ferryBookingDao.deleteBooking(booking)
    }

    suspend fun clearAll() {
        ferryBookingDao.clearAllBookings()
    }
}
