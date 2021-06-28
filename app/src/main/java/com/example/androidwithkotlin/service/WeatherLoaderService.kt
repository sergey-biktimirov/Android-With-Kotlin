package com.example.androidwithkotlin.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.androidwithkotlin.extension.TAG
import com.example.androidwithkotlin.constants.WeatherConstants
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
                    val lat = intent.getDoubleExtra(WeatherConstants.Extras.LATITUDE, 0.0)
                    val lon = intent.getDoubleExtra(WeatherConstants.Extras.LONGITUDE, 0.0)

                    loadWeatherByLatAndLon(lat, lon)
                }
            }
        }
    }

    /**
     * Load weather by coordinates
     * @param lat latitude
     * @param lon longitude
     */
    private fun loadWeatherByLatAndLon(lat: Double, lon: Double) {
        sendBroadcastWeatherLoading(true)

        Log.d(
            TAG(), """lat = $lat
                        |lon = $lon
                    """.trimMargin()
        )

        Log.d(TAG(), "weather loading....")

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

    /**
     * Send broadcast message to
     * [com.example.androidwithkotlin.broadcast_receiver.WeatherBroadcastReceiver],
     * that the weather is loaded
     * @param weather Weather DTO
     */
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
                this.putExtra(
                    WeatherConstants.Extras.ICON,
                    weather.icon
                )
            }
        )
    }

    /**
     * Send broadcast message to
     * [com.example.androidwithkotlin.broadcast_receiver.WeatherBroadcastReceiver],
     * that the weather is loading
     * @param isLoading true - weather is loading, false - not
     */
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