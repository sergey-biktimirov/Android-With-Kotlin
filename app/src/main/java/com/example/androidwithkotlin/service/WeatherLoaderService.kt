package com.example.androidwithkotlin.service

import android.app.IntentService
import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidwithkotlin.extension.TAG
import com.example.androidwithkotlin.intent.WeatherConstants
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Weather
import com.example.androidwithkotlin.repository.IWeatherRepository
import com.example.androidwithkotlin.repository.YandexWeatherRepository
import kotlinx.coroutines.*
import java.lang.Exception

class WeatherLoaderService : Service() {
    private val localBroadcastManager = LocalBroadcastManager.getInstance(this)
    private val weatherRepository: IWeatherRepository = YandexWeatherRepository()

    init {
        Log.d(TAG(), "initial service")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        onHandleIntent(intent)
        return super.onStartCommand(intent, flags, startId)
    }

    private fun onHandleIntent(intent: Intent?) {
        Log.d(TAG(), "onHandleIntent, action = ${intent?.action ?: "no intent"}")

        intent?.let {
            when (it.action) {
                WeatherConstants.Action.LOAD_WEATHER_BY_COORDINATES -> {
                    val lat = intent.getFloatExtra(WeatherConstants.Extras.LATITUDE, 0f)
                    val lon = intent.getFloatExtra(WeatherConstants.Extras.LONGITUDE, 0f)

                    loadWeatherByCity(lat, lon)
                }
            }
        }
    }

    private fun loadWeatherByCity(lat: Float, lon: Float) {
        sendBroadcastWeatherLoading(true)

        Log.d(
            TAG(), """lat = $lat
                        |lon = $lon
                    """.trimMargin()
        )

        Log.d(TAG(), "weather loadeding....")

        CoroutineScope(Dispatchers.Main + SupervisorJob()).launch {
            try {
                val weather: Weather? =
                    weatherRepository.getByCity(City("//TODO city name", lat, lon))
                if (weather == null) {
                    //TODO ERROR EXTRA
                } else {
                    sendBroadcastWeatherLoading(false)
                    sendBroadcastWeatherLoaded(weather)
                }
            } catch (e: Exception) {
                sendBroadcastWeatherLoading(false)
                //TODO ERROR EXTRA
            }
        }
    }

    private fun sendBroadcastWeatherLoaded(weather: Weather) {
        localBroadcastManager.sendBroadcast(
            Intent(WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADED).apply {
                this.putExtra(
                    WeatherConstants.Extras.TEMPERATURE,
                    weather.temperature
                )
                this.putExtra(
                    WeatherConstants.Extras.FEELS_LIKE,
                    weather.feelsLike
                )
            }
        )
    }

    private fun sendBroadcastWeatherLoading(isLoading: Boolean) {
        localBroadcastManager.sendBroadcast(
            Intent(WeatherConstants.Action.WEATHER_BY_COORDINATES_LOADING).apply {
                this.putExtra(WeatherConstants.Extras.WEATHER_LOADING, isLoading)
            }
        )
    }

    companion object {
        val NAME = "com.example.androidwithkotlin.service.WeatherLoaderService"
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}