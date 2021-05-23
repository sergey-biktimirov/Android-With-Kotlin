package com.example.androidwithkotlin.model

class WeatherRepositoryImpl : WeatherRepository {

    override fun getFromServer(): Weather {
        return Weather()
    }

    override fun getFromLocalStorage(): Weather {
        return Weather()
    }
}
