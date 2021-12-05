package com.udacity.asteroidradar.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.api.ApiClient
import com.udacity.asteroidradar.api.NasaApiService
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDao
import com.udacity.asteroidradar.database.AsteroidDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AsteroidRepository(private val asteroidDao: AsteroidDao, private val apiService: NasaApiService) {

    val asteroidsLiveData: LiveData<List<Asteroid>> = Transformations.map(asteroidDao.listAll()) {
        it.map { asteroid -> asteroid.toModel() }
    }

    suspend fun fetchAsteroids() {
        withContext(Dispatchers.IO) {
            val asteroids = fetchApiAsteroids()
            saveAsteroids(asteroids)
        }
    }

    suspend fun getPicOfTheDay() = apiService.getPictureOfTheDay()

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
        fun create(application: Application): AsteroidRepository {
            val database = AsteroidDatabase.getInstance(application.applicationContext)
            val api = ApiClient.nasaApiService
            return AsteroidRepository(database.asteroidDao, api)
        }
    }
}