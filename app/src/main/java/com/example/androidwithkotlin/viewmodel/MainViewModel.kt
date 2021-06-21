package com.example.androidwithkotlin.viewmodel

import android.location.Geocoder
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.androidwithkotlin.extension.getApplicationContext
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.repository.FakeCityRepository
import com.example.androidwithkotlin.repository.ICityRepository
import kotlinx.coroutines.launch
import java.lang.Exception

class MainViewModel(
    private val cityRepository: ICityRepository = FakeCityRepository()
) : ViewModel() {

    private val _weatherLiveData = MutableLiveData<AppState>()
    val weatherLiveData: LiveData<AppState>
        get() = _weatherLiveData

    /** Set what a weather will be shown
     * true - world weather
     * false - Russian weather
     * */
    val isWorldWeather = MutableLiveData(true)

    fun loadAllCities() {
        viewModelScope.launch {
            _weatherLiveData.value = AppState.Loading
            _weatherLiveData.postValue(AppState.Success(cityRepository.getAll()))
        }
    }

    fun loadCitiesByCountry(country: Country) {
        viewModelScope.launch {
            _weatherLiveData.value = AppState.Loading
            _weatherLiveData
                .postValue(
                    AppState.Success(
                        cityRepository.getByCountry(
                            country.country
                        )
                    )
                )
        }
    }
}
