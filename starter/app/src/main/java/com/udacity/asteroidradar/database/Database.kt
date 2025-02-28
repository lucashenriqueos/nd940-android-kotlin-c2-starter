package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AsteroidEntity::class, PictureOfDayEntity::class], version = 1)
abstract class AsteroidDatabase : RoomDatabase() {

    abstract val asteroidDao: AsteroidDao

    abstract val pictureOfDayDao: PictureOfDayDao

    companion object {
        private lateinit var instance: AsteroidDatabase

        fun getInstance(context: Context): AsteroidDatabase {
            synchronized(AsteroidDatabase::class.java) {
                if (::instance.isInitialized.not()) {
                    instance = Room.databaseBuilder(context, AsteroidDatabase::class.java, "asteroid_db")
                        .build()
                }
                return instance
            }
        }
    }
}