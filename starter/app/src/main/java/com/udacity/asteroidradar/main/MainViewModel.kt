package com.udacity.asteroidradar.main

import android.app.Application
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
    val asteroidsLiveData: LiveData<List<Asteroid>> = repository.asteroidsLiveData

    val picOfTheDayLiveData: LiveData<PictureOfDay> = repository.pictureOfDayLiveData

    init {
        viewModelScope.launch {
            repository.fetchAsteroids()
            repository.fetchPicOfTheDay()
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