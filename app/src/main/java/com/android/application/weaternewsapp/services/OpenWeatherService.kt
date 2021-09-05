package com.android.application.weaternewsapp.services

import com.android.application.weaternewsapp.BuildConfig
import com.android.application.weaternewsapp.services.dto.WeatherResponse
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface OpenWeatherService {

    @GET("weather?units=metric")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") long: Double,
        @Query("apiKey") appid: String,
    ) : Response<WeatherResponse>
}

// here use the dagger hilt service injection
//fun OpenWeatherService(): OpenWeatherService = Retrofit.Builder()
//        .baseUrl("https://api.openweathermap.org/data/2.5/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create()

//fun OpenWeatherService(): OpenWeatherService {
//    return Retrofit.Builder()
//        .baseUrl("api.openweathermap.org/data/2.5/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//        .create()
//}