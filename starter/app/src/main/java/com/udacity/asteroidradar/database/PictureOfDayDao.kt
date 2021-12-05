package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface PictureOfDayDao {

    @Query("select * from PictureOfDayEntity order by Date(insertionDate) limit 1")
    fun findLatestPic(): LiveData<PictureOfDayEntity>

    @Insert(onConflict = REPLACE)
    fun save(vararg pic: PictureOfDayEntity)
}