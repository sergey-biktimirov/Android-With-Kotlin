package com.example.androidwithkotlin.model

/**Данные города
 * @param city Наименование города
 * @param latitude Широта
 * @param longitude Долгота
 * */
data class City(
    val city: String,
    val latitude: Double,
    val longitude: Double
)
