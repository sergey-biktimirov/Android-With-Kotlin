package com.example.androidwithkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidwithkotlin.R
import com.example.androidwithkotlin.extension.getStringResource
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.repository.IWeatherRepository
import com.example.androidwithkotlin.repository.YandexWeatherRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class WeatherDetailsViewModel : ViewModel() {
    private var _city: City? = null
    private val city: City = _city ?: City(
        city = getStringResource(R.string.filed_to_load_city_info),
        latitude = 0f,
        longitude = 0f,
        country = Country("Unknown")
    )

    private val _weatherState = MutableLiveData<AppState>()
    private val weatherRepository: IWeatherRepository = YandexWeatherRepository()

    val weatherState: LiveData<AppState> = _weatherState

    fun loadWeatherByCity(city: City) {
        this._city = city.copy()

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
        loadWeatherByCity(this.city)
    }
}