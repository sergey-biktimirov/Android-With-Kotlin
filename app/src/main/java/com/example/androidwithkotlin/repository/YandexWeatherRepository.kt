package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.api.YandexWeatherAPI
import com.example.androidwithkotlin.api.YandexWeatherDTO
import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Weather
import okhttp3.ResponseBody
import retrofit2.awaitResponse
import java.lang.Exception

class YandexWeatherRepository : IWeatherRepository {
    private val yandexWeatherService = YandexWeatherAPI.retrofitService

    override suspend fun getByCity(city: City): Weather {
        val response = yandexWeatherService.get(
            lat = city.latitude!!,
            lon = city.longitude!!
        ).awaitResponse()

        if (response.isSuccessful) {
            val weatherDTO = response.body() as YandexWeatherDTO
            return Weather(
                city = city,
                temperature = weatherDTO.fact?.temp ?: 0,
                feelsLike = weatherDTO.fact?.feelsLike ?: 0,
                icon = weatherDTO.fact?.icon ?: ""
            )
        } else {
            val errorBody = response.errorBody() as ResponseBody
            val exceptionMessage = errorBody.string()
            throw Exception(exceptionMessage)
        }
    }
}