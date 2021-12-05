package com.udacity.asteroidradar.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.udacity.asteroidradar.PictureOfDay

@Entity(tableName = "PictureOfDayEntity")
data class PictureOfDayEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long? = null,

    @ColumnInfo(name = "mediaType")
    val mediaType: String?,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "url")
    val url: String?,

    @ColumnInfo(name = "insertionDate")
    val insertionDate: String
) {
    fun toModel() = PictureOfDay(
        mediaType.orEmpty(), title.orEmpty(), url.orEmpty()
    )
}