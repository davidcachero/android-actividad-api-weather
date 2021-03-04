package com.example.tareaapiweatherdavid.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Cities::class, Users::class, UserCitiesCrossRef::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao():UserDao

    companion object {
        private const val DATABASE_NAME = "BBDDavid_OpenWeather"

        @Volatile
        private var INSTANCE:AppDatabase? = null
        fun getInstance(context:Context):AppDatabase? {
            INSTANCE ?: synchronized(this) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext, AppDatabase::class.java, DATABASE_NAME
                ).build()
            }
            return INSTANCE
        }
    }
}