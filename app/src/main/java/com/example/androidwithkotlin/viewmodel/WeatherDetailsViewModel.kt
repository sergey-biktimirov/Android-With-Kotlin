package com.example.androidwithkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.extension.TAG
import com.example.androidwithkotlin.extension.getStringResource
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.repository.IWeatherRepository
import com.example.androidwithkotlin.repository.YandexWeatherRepository
import kotlinx.coroutines.launch
import kotlin.Exception

class WeatherDetailsViewModel : ViewModel() {
    var city: City? = null

    private val _weatherState = MutableLiveData<AppState>()
    private val weatherRepository: IWeatherRepository = YandexWeatherRepository()

    val weatherState: LiveData<AppState> = _weatherState

    fun loadWeatherByCity(city: City) {
        this.city = city.copy()

        Log.d(TAG(), "${this.city}")

        viewModelScope.launch {
            _weatherState.postValue(AppState.Loading)

            try {
                val loadedWeather = weatherRepository.getByCity(city)

                if (loadedWeather != null) {
                    _weatherState.postValue(AppState.Success(loadedWeather))
                } else {
                    _weatherState.postValue(
                        AppState.Error(
                            Exception(
                                getStringResource(
                                    R.string.filed_to_load_forecast_for_city,
                                    city.city
                                )
                            )
                        )
                    )
                }
            } catch (t: Throwable) {
                _weatherState.postValue(
                    AppState.Error(t)
                )
            }
        }
    }

    fun reloadDataByCity() {
        if (city != null) {
            loadWeatherByCity(city!!)
        } else {
            throw Exception("city is null")
        }
    }
}