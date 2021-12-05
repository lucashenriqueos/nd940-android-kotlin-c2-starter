package com.udacity.asteroidradar

import com.squareup.moshi.Json
import com.udacity.asteroidradar.database.PictureOfDayEntity

data class PictureOfDay(
    @Json(name = "media_type") val mediaType: String,
    val title: String,
    val url: String
) {

    fun toEntity(date: String): PictureOfDayEntity = PictureOfDayEntity(
        mediaType = mediaType,
        title = title,
        url = url,
        insertionDate = date
    )
}