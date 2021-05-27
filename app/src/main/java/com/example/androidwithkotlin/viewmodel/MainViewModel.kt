package com.example.androidwithkotlin.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.repository.IWeatherRepository
import com.example.androidwithkotlin.repository.FakeWeatherRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainViewModel(
    private val weatherRepository: IWeatherRepository = FakeWeatherRepository()
) : ViewModel() {
    private val _weatherLiveData = MutableLiveData<AppState>()
    val weatherLiveData: LiveData<AppState>
        get() = _weatherLiveData

    fun loadAllWeather() {
        viewModelScope.launch {
            _weatherLiveData.value = AppState.Loading
            delay(1000)
            _weatherLiveData.postValue(AppState.Success(weatherRepository.getAll()))
        }
    }

    fun loadAllWeatherByCountry(country: String) {
        viewModelScope.launch {
            _weatherLiveData.value = AppState.Loading
            delay(1000)
            _weatherLiveData
                .postValue(
                    AppState.Success(
                        weatherRepository.getByCountry(
                            Country(
                                country = country
                            )
                        )
                    )
                )
        }
    }
}
