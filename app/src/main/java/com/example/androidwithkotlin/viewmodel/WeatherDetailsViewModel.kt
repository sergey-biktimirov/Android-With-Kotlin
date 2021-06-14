package com.example.androidwithkotlin.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import com.example.androidwithkotlin.extension.TAG
import com.example.androidwithkotlin.extension.getStringResource
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.repository.IWeatherRepository
import com.example.androidwithkotlin.repository.IWeatherViewHistoryRepository
import kotlinx.coroutines.launch
import kotlin.Exception

class WeatherDetailsViewModel(
    private val weatherRepository: IWeatherRepository,
    private val weatherViewHistoryRepository: IWeatherViewHistoryRepository
) : ViewModel() {

    var city: City? = null

    private val _weatherState = MutableLiveData<AppState>()

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
                    weatherViewHistoryRepository.post(
                        WeatherViewHistoryEntity(
                            city = loadedWeather.city.city,
                            temperature = loadedWeather.temperature,
                            feelsLike = loadedWeather.feelsLike
                        )
                    )
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

class WeatherDetailsViewModelFactory(
    private val weatherRepository: IWeatherRepository,
    private val weatherViewHistoryRepository: IWeatherViewHistoryRepository
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherDetailsViewModel::class.java)) {
            return WeatherDetailsViewModel(
                weatherRepository,
                weatherViewHistoryRepository
            ) as T
        } else {
            throw IllegalArgumentException("Unknown view model class")
        }
    }

}