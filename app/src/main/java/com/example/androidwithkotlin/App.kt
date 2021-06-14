package com.example.androidwithkotlin

import android.app.Application
import com.example.androidwithkotlin.db.room.WeatherRoomDataBase

class App : Application() {

    val roomDataBase by lazy {
            WeatherRoomDataBase.getDataBase(this)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: App
    }
}