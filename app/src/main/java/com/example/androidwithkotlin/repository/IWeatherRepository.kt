package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.model.Weather

/** Репозиторий данных погоды
 * */
interface IWeatherRepository {

    /** Получить данные о погоде по всем городам
     * */
    fun getAll(): List<Weather>

    /** Получить данные о погоде по городу
     * */
    fun getByCity(city: City): Weather?

    /** Получить данные о погоде по стране
     * */
    fun getByCountry(country: Country): List<Weather>

    //TODO Получить погоду по региону или штату
}
