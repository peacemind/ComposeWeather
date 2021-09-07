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
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import com.android.application.weaternewsapp.services.WeatherRepository
import com.android.application.weaternewsapp.services.dto.WeatherResponse
import com.android.application.weaternewsapp.ui.theme.Cloudy
import com.android.application.weaternewsapp.ui.theme.Rainy
import com.android.application.weaternewsapp.ui.theme.SunnyGreen
import com.android.application.weaternewsapp.ui.theme.WeaterNewsAppTheme
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.tasks.Task
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest
import kotlin.math.roundToInt

@AndroidEntryPoint
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
                    weather?.let {
                        WeatherSummary(weather = it)
                        TemperatureSummary(it)
                        Divider()
                    }
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(weather?.backgroundColor() ?: Color.Green)
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
        Column(
            Modifier
                .padding(top = 48.dp)
                .align(Alignment.TopCenter),
            horizontalAlignment = Alignment.CenterHorizontally) {
            // "\u2103" -> degree C , "\u2109" -> degree F, "\u00B0" -> degree
            //Text(text = stringResource(R.string.temperature_degree, weather.main.temp), fontSize = 48.sp, color = Color.White)
            Text(text = formatTemperature(weather.main.temp), fontSize = 48.sp, color = Color.White)
            Text(text = weather.name, fontSize = 28.sp, color = Color.White)
            Text(text = weather.weather.first().main, fontSize = 18.sp, color = Color.White)
        }
    }
}

@Composable
fun TemperatureSummary(weather: WeatherResponse) {
    Row(
        Modifier
            .fillMaxWidth()
            .background(weather.backgroundColor())
            .padding(horizontal = 30.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = formatTemperature(weather.main.tempMin), color = Color.White)
            Text(text = stringResource(R.string.min_temperature), color = Color.White)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = formatTemperature(weather.main.temp), color = Color.White)
            Text(text = stringResource(R.string.current_temperature), color = Color.White)

        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = formatTemperature(weather.main.tempMax), color = Color.White)
            Text(text = stringResource(R.string.max_temperature), color = Color.White)
        }
    }
}

@Composable
private fun formatTemperature(temperature: Double): String {
    return stringResource(R.string.temperature_degree, temperature.roundToInt())
}

//private fun WeatherResponse.formatTemperature(): String {
//    return main.temp.roundToInt().toString()
//}

@DrawableRes
private fun WeatherResponse.background(): Int {
    val condition: String = weather.first().main
    println(condition)
    return when {
        condition.contains("cloud", ignoreCase = true) -> R.drawable.forest_cloudy
        condition.contains("rain", ignoreCase = true) -> R.drawable.forest_rainy
        else -> R.drawable.forest_sunny
    }
}

private fun WeatherResponse.backgroundColor(): Color {
    val condition: String = weather.first().main
    println(condition)
    return when {
        condition.contains("cloud", ignoreCase = true) -> Cloudy
        condition.contains("rain", ignoreCase = true) -> Rainy
        else -> SunnyGreen
    }
}