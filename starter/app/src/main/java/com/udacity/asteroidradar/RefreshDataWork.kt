package com.udacity.asteroidradar

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.repository.AsteroidRepository

class RefreshDataWork(
    private val context: Context,
    private val params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val repository = AsteroidRepository.create(context)
        return try {
            repository.fetchAsteroids()
            repository.fetchPicOfTheDay()
            Log.d("RefreshDataWork", "Refreshing data succeed")
            Result.success()
        } catch (e: Exception) {
            Log.d("RefreshDataWork", "Refreshing data failed")
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

}