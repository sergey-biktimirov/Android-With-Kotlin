package com.example.androidwithkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
) : Parcelable

fun getDefaultCity() =
    City(
        "Москва",
        55.755826f,
        37.617299900000035f,
        Country("Россия", listOf())
    )
