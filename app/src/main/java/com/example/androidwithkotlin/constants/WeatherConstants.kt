package com.example.androidwithkotlin.constants

class WeatherConstants {
    object Action {
        val WEATHER_BY_COORDINATES_LOADING = "com.example.androidwithkotlin.broadcast_receiver" +
                ".WeatherBroadcastReceiver.WEATHER_BY_COORDINATES_LOADEDING"
        val WEATHER_BY_COORDINATES_LOADED = "com.example.androidwithkotlin.broadcast_receiver" +
                ".WeatherBroadcastReceiver.WEATHER_BY_COORDINATES_LOADED"
        val LOAD_WEATHER = "com.example.androidwithkotlin.broadcast_receiver" +
                ".WeatherBroadcastReceiver.LOAD_WEATHER"

        val LOAD_WEATHER_BY_COORDINATES =
            "com.example.androidwithkotlin.service.WeatherLoaderService.LOAD_WEATHER_BY_COORDINATES"
    }

    object Extras {
        val WEATHER_LOADING = "WEATHER_LOADING"
        val LATITUDE = "LATITUDE"
        val LONGITUDE = "LONGITUDE"
        val TEMPERATURE: String = "TEMPERATURE"
        val FEELS_LIKE: String = "FEELS_LIKE"
        val ICON: String = "ICON"
        val ERROR_LOADING_WEATHER = "ERROR_LOADING_WEATHER"
        val WEATHER_CITY_NAME = "WEATHER_CITY_NAME"
    }

    object Preferences {
        val IS_WORLD_WEATHER_KEY = "IS_WORLD_WEATHER_KEY"
    }

    enum class RequestCodes {
        READ_CONTACTS
    }


    object LocationManager {
        /** Minimum time interval between location updates in milliseconds
         * */
        val REFRESH_PERIOD = 60000L

        /** Minimum distance between location updates in meters
         * */
        val MINIMAL_DISTANCE = 100f
    }
}