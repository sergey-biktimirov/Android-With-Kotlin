package com.example.androidwithkotlin.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androidwithkotlin.model.WeatherRepository
import com.example.androidwithkotlin.model.WeatherRepositoryImpl
import java.lang.Thread.sleep

class MainViewModel(
    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData(),
    private val weatherRepositoryImpl: WeatherRepository = WeatherRepositoryImpl()
) :
    ViewModel() {

    fun getLiveData() = liveDataToObserve

    fun getWeatherFromLocalSource() = getWeatherDataFromLocalSource()

    fun getWeatherFromRemoteSource() = getWeatherDataFromLocalSource()

    private fun getWeatherDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading
        Thread {
            sleep(1000)
            liveDataToObserve.postValue(AppState.Success(weatherRepositoryImpl.getFromLocalStorage()))
        }.start()
    }
}
