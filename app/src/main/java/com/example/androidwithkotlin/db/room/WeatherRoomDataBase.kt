package com.example.androidwithkotlin.db.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.androidwithkotlin.db.room.dao.IWeatherViewHistoryDAO
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity

@Database(
    entities = [
        WeatherViewHistoryEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class WeatherRoomDataBase : RoomDatabase() {

    abstract fun weatherViewHistoryDAO(): IWeatherViewHistoryDAO

    companion object {
        private var INSTANCE: WeatherRoomDataBase? = null

        fun getDataBase(context: Context): WeatherRoomDataBase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE = Room
                    .databaseBuilder(
                        context.applicationContext,
                        WeatherRoomDataBase::class.java,
                        "weather_room_database"
                    ).build()
                INSTANCE!!
            }
        }
    }
}