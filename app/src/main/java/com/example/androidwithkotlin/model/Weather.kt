package com.example.androidwithkotlin.model

import android.os.Parcelable
import android.widget.ImageView
import com.example.androidwithkotlin.BuildConfig
import com.example.androidwithkotlin.extension.getActivity
import com.example.androidwithkotlin.extension.parseUri
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
    val icon: String = ""
) : Parcelable {
    fun showWeatherIcon(view: ImageView) {
        GlideToVectorYou
            .justLoadImageAsBackground(
                getActivity(),
                BuildConfig.YANDEX_WEATHER_ICON_URL.format(icon).parseUri(),
                view
            )
    }
}

fun getDefaultCity() =
    City(
        "Москва",
        55.755826,
        37.617299900000035,
        Country("Россия", listOf())
    )
