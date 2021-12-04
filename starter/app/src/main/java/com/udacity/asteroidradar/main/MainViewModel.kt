package com.udacity.asteroidradar.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.Constants
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.api.ApiClient
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class MainViewModel : ViewModel() {
    private val _picOfTheDayLiveData = MutableLiveData<PictureOfDay>()
    val picOfTheDayLiveData: LiveData<PictureOfDay>
        get() = _picOfTheDayLiveData

    private val _asteroidsLiveData = MutableLiveData<List<Asteroid>>()
    val asteroidsLiveData: LiveData<List<Asteroid>>
        get() = _asteroidsLiveData

    init {
        getPicOfTheDay()
        listAsteroids()
    }

    private fun listAsteroids() {
        viewModelScope.launch {
            try {
                val currentTime = Calendar.getInstance().time
                val formatter = SimpleDateFormat(Constants.API_QUERY_DATE_FORMAT, Locale.getDefault())
                val result = ApiClient.nasaApiService.getFeed(formatter.format(currentTime))
                val asteroids = parseAsteroidsJsonResult(JSONObject(result))
                _asteroidsLiveData.postValue(asteroids)
            } catch (exception: Exception) {
                Log.e("MainViewModel", exception.toString())
            }
        }
    }

    private fun getPicOfTheDay() {
        viewModelScope.launch {
            try {
                val imgOfTheDay = ApiClient.nasaApiService.getPictureOfTheDay()
                _picOfTheDayLiveData.postValue(imgOfTheDay)
            } catch (e: Exception) {
                Log.e("MainViewModel", e.toString())
            }
        }
    }
}