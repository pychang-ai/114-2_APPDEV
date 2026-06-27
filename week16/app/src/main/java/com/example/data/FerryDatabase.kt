package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [FerryBooking::class], version = 1, exportSchema = false)
abstract class FerryDatabase : RoomDatabase() {
    abstract fun ferryBookingDao(): FerryBookingDao

    companion object {
        @Volatile
        private var INSTANCE: FerryDatabase? = null

        fun getDatabase(context: Context): FerryDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    FerryDatabase::class.java,
                    "ferry_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
