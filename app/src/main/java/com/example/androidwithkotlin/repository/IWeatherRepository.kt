package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.model.Weather

/** Репозиторий данных погоды
 * */
interface IWeatherRepository {

    /** Получить данные о погоде по городу
     * */
    suspend fun getByCity(city: City): Weather?
}
