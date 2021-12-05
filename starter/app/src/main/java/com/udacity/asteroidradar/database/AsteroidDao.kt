package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.udacity.asteroidradar.Asteroid

@Dao
interface AsteroidDao {

    @Query("select * from AsteroidEntity where Date(closeApproachDate) >= Date() order by closeApproachDate")
    fun listAll(): LiveData<List<AsteroidEntity>>

    @Query("select * from AsteroidEntity where id = :id")
    fun getById(id: Long): AsteroidEntity

    @Insert(onConflict = REPLACE)
    fun save(asteroids: List<AsteroidEntity>)
}