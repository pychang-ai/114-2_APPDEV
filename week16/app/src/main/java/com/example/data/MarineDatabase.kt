package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MarineSpecies::class], version = 1, exportSchema = false)
abstract class MarineDatabase : RoomDatabase() {
    abstract fun marineDao(): MarineDao

    companion object {
        @Volatile
        private var INSTANCE: MarineDatabase? = null

        fun getDatabase(context: Context): MarineDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MarineDatabase::class.java,
                    "marine_encyclopedia_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
