package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.repository.AsteroidRepository
import kotlinx.coroutines.launch

class MainViewModel(private val repository: AsteroidRepository) : ViewModel() {
    private val _picOfTheDayLiveData = MutableLiveData<PictureOfDay>()
    val picOfTheDayLiveData: LiveData<PictureOfDay>
        get() = _picOfTheDayLiveData

    val asteroidsLiveData: LiveData<List<Asteroid>> = repository.asteroidsLiveData

    init {
        viewModelScope.launch {
            repository.fetchAsteroids()
            getPicOfTheDay()
        }
    }

    private fun getPicOfTheDay() {
        viewModelScope.launch {
            try {
                val imgOfTheDay = repository.getPicOfTheDay()
                _picOfTheDayLiveData.postValue(imgOfTheDay)
            } catch (e: Exception) {
                Log.e("MainViewModel", e.toString())
            }
        }
    }

    class Factory(private val application: Application) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                val repository = AsteroidRepository.create(application.applicationContext)
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}