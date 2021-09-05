package com.android.application.weaternewsapp.services

import android.Manifest
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.annotation.RequiresPermission
import com.android.application.weaternewsapp.BuildConfig
import com.android.application.weaternewsapp.services.dto.WeatherResponse
import com.google.android.gms.location.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import javax.inject.Inject


class WeatherRepository @Inject constructor(
    private val service: OpenWeatherService
){
//    private val currentLocation = MutableStateFlow<Location?>(null)
//    private val service = OpenWeatherService()
//    private val weather = MutableStateFlow<WeatherResponse?>(null)
//    fun getWeather(): Flow<WeatherResponse?> = weather

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    fun currentLocationWeather(context: Context): Flow<WeatherResponse?> {
        return locationFlow(context).map {
            println("lat = ${it.latitude}")
            println("lon = ${it.longitude}")
            service.getCurrentWeather(it.latitude, it.longitude, BuildConfig.API_KEY)
                .body()
        }
    }
//        locationFlow(context).onEach {
//            val response = service.getCurrentWeather(it.latitude, it.longitude, BuildConfig.API_KEY)
//            val body = response.body()
//            if (response.isSuccessful && body != null) {
//                weather.value = body
//            }
//        }

    @RequiresPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
    private fun locationFlow(context: Context):Flow<Location> = channelFlow<Location> {
        val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                println("Received $result")
                trySend(result.lastLocation)
               }
            }
        val request: LocationRequest = LocationRequest.create()
            .setInterval(10_000)
            .setFastestInterval(5_000)
            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
            .setSmallestDisplacement(170f)

        client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose {
            println("Cancelled")
            client.removeLocationUpdates(callback)
        }
    }
}