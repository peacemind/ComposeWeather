package com.android.application.weaternewsapp

import android.annotation.SuppressLint
import android.content.IntentSender
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModelProvider
import com.android.application.weaternewsapp.services.WeatherRepository
import com.android.application.weaternewsapp.services.dto.WeatherResponse
import com.android.application.weaternewsapp.ui.theme.WeaterNewsAppTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import java.util.jar.Manifest

class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    private val requestPermission: ActivityResultLauncher<String?> = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
//        if (it) viewModel.onPermissionGranted()
        // Todo: fetch current location if granted
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeaterNewsAppTheme {
                val weather:WeatherResponse? by viewModel.weather.collectAsState(null)
                Column(Modifier.fillMaxSize()) {
                    weather?.let { WeatherSummary(weather = it) }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF47AB2F))
                    ) {

                        Text(text = "Todo")
                    }
                }
                requestPermission.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)

//                ViewModelProvider(this).get(MainViewModel::class.java)
            }
        }
    }
}

@Composable
fun WeatherSummary(weather: WeatherResponse) {
    Box {
        Image(
            painter = painterResource(id = weather.background()),
            //painter = painterResource(id = R.drawable.forest_sunny),
            contentDescription = "Background",
            modifier = Modifier.fillMaxWidth(),
            //modifier = Modifier.fillMaxWidth().background(Color(0xFF47AB2F)),
            contentScale = ContentScale.FillWidth
        )
        Column(Modifier.align(Alignment.Center)) {
            Text(text = weather.main.temp.toString())
            Text(text = weather.name.toString())
            Text(text = weather.weather.toString())
        }
    }
}

private fun WeatherResponse.background(): Int {
    val condition: String = weather.first().main
    println(condition)
    return when {
        condition.contains("cloud", ignoreCase = true) -> R.drawable.forest_cloudy
        condition.contains("rain", ignoreCase = true) -> R.drawable.forest_rainy
        else -> R.drawable.forest_sunny
    }
}