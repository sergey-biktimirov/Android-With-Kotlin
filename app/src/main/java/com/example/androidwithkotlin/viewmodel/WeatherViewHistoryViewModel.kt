package com.example.androidwithkotlin.viewmodel

import androidx.lifecycle.*
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import com.example.androidwithkotlin.exception.viewmodel.UnknownViewModelClassException
import com.example.androidwithkotlin.repository.IWeatherViewHistoryRepository
import kotlinx.coroutines.launch

class WeatherViewHistoryViewModel(private val repository: IWeatherViewHistoryRepository) :
    ViewModel() {

    fun getAll(): LiveData<List<WeatherViewHistoryEntity>> =
        repository.getAll().asLiveData()

    fun getByCityName(name: String): LiveData<List<WeatherViewHistoryEntity>> =
        repository.getByCityName(name).asLiveData()

    fun post(entity: WeatherViewHistoryEntity) = viewModelScope.launch {
        repository.post(entity)
    }

    fun put(entity: WeatherViewHistoryEntity) = viewModelScope.launch {
        repository.put(entity)
    }

    fun delete(entity: WeatherViewHistoryEntity) = viewModelScope.launch {
        repository.delete(entity)
    }
}

class WeatherViewHistoryViewModelFactory(private val repository: IWeatherViewHistoryRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WeatherViewHistoryViewModel::class.java)) {
            return WeatherViewHistoryViewModel(repository) as T
        } else {
            throw UnknownViewModelClassException()
        }
    }

}