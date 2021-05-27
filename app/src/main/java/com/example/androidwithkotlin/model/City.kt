package com.example.androidwithkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**Данные города
 * @param city Наименование города
 * @param latitude Широта
 * @param longitude Долгота
 * @param country Страна
 * */
@Parcelize
data class City(
    val city: String,
    val latitude: Double,
    val longitude: Double,
    val country: Country
    //TODO регион или штат
) : Parcelable
