package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ferry_bookings")
data class FerryBooking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val departure: String,
    val destination: String,
    val passengerName: String,
    val seatNumber: String,
    val price: Int,
    val isConcession: Boolean,
    val bookingCode: String,
    val bookingTime: Long = System.currentTimeMillis()
)
