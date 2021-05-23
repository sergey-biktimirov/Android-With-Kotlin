package com.example.androidwithkotlin.model

/** Репозиторий данных погоды
 * */
interface WeatherRepository {

    /** Получить данные о погоде с вервера
     * */
    fun getFromServer(): Weather

    /** Получить данные о погоде из локального хранилища
     * */
    fun getFromLocalStorage(): Weather
}
