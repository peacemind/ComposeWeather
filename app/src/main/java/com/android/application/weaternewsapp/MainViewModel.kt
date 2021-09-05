package com.android.application.weaternewsapp

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.android.application.weaternewsapp.services.OpenWeatherService
import com.android.application.weaternewsapp.services.WeatherRepository
import com.android.application.weaternewsapp.services.dto.WeatherResponse
import com.google.android.gms.location.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@SuppressLint("MissingPermission")
@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    repository: WeatherRepository
) : AndroidViewModel(application) {
    //    private var lastLocation: Location? = null
    // private var repo = WeatherRepository()

    val weather: Flow<WeatherResponse?> = repository.currentLocationWeather(getApplication())
}
//    fun onPermissionGranted(): Flow<WeatherResponse?> {
//        return repo.currentLocationWeather(getApplication() as Context)
//        val weatherRepository = WeatherRepository()
//        viewModelScope.launch {
//            weatherRepository.currentLocationWeather(getApplication() as Context).collect {
//                val r = OpenWeatherService().getCurrentWeather(
//                    it.latitude,
//                    it.longitude,
//                    BuildConfig.API_KEY
//                )
//                println(r.code())
//                println(r.body())
//            }
//        }

//        viewModelScope.launch {
//            WeatherRepository().getWeather().collect {
//                println(it)
//            }
//        }

//        val application: Application = getApplication()
//        val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(application as Context)
//        val request: LocationRequest = LocationRequest.create()
//            .setInterval(10_000)
//            .setFastestInterval(5_000)
//            .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
//            .setSmallestDisplacement(170f)
//
//        client.requestLocationUpdates(request, object : LocationCallback() {
//            override fun onLocationResult(result: LocationResult) {
//                lastLocation = result.lastLocation
//                GlobalScope.launch {
//                    println(lastLocation?.latitude)
//                    println(lastLocation?.longitude)
//
//                    val response = OpenWeatherService().getCurrentWeather(
//                        lastLocation?.latitude ?: 0.0,
//                        lastLocation?.longitude ?: 0.0,
//                        BuildConfig.API_KEY
//                    )
//
//                    // println(response.code())
//                    println(response.body())
//                }
//                println("Result: ${result.lastLocation}")
//            }
//
//            override fun onLocationAvailability(availability: LocationAvailability) {
//                println("Availability: ${availability.isLocationAvailable}")
//            }
//        }, Looper.getMainLooper())
