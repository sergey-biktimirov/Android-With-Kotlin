package com.example.androidwithkotlin.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Country(
    var country: String,
    var cityList: List<City> = listOf()
) : Parcelable