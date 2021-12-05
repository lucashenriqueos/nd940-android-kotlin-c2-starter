package com.udacity.asteroidradar.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApiClient
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import com.udacity.asteroidradar.database.PictureOfDayDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(
    private val asteroidDao: AsteroidDao,
    private val picDao: PictureOfDayDao,
    private val apiService: NasaApiService
) {

    val asteroidsLiveData: LiveData<List<Asteroid>> = Transformations.map(asteroidDao.listAll()) {
        it.map { asteroid -> asteroid.toModel() }
    }

    val pictureOfDayLiveData: LiveData<PictureOfDay> = Transformations.map(picDao.findLatestPic()) {
        it?.toModel()
    }

    suspend fun fetchAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroids = fetchApiAsteroids()
                saveAsteroids(asteroids)
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    suspend fun fetchPicOfTheDay() {
        withContext(Dispatchers.IO) {
            try {
                val currentTime = Calendar.getInstance().time
                val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                val date = formatter.format(currentTime)
                val pic = getPicOfTheDay()
                picDao.save(pic.toEntity(date))
            } catch (exception: Exception) {
                exception.printStackTrace()
            }
        }
    }

    private suspend fun getPicOfTheDay() = apiService.getPictureOfTheDay()

    private suspend fun fetchApiAsteroids(): ArrayList<Asteroid> {
        val currentTime = Calendar.getInstance().time
        val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
        val result = apiService.getFeed(formatter.format(currentTime))
        return parseAsteroidsJsonResult(JSONObject(result))
    }

    private fun saveAsteroids(asteroids: List<Asteroid>) {
        val entities = asteroids.map { it.toEntity() }
        asteroidDao.save(entities)
    }

    companion object {
        fun create(context: Context): AsteroidRepository {
            val database = AsteroidDatabase.getInstance(context)
            val api = ApiClient.nasaApiService
            return AsteroidRepository(database.asteroidDao, database.pictureOfDayDao, api)
        }
    }
}