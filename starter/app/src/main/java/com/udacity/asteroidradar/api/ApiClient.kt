package com.udacity.asteroidradar.api

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object ApiClient {

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor {
                Log.d("OkHttp", "Requesting for data")
                Log.d("OkHttp", it.request().url().toString())
                it.proceed(it.request())
            }
            .build()
    }

    private val retrofitService by lazy {
        Retrofit.Builder()
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .client(httpClient)
            .baseUrl(Constants.BASE_URL)
            .build()
    }

    val nasaApiService: NasaApiService by lazy {
        retrofitService.create(NasaApiService::class.java)
    }
}