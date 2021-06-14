package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import kotlinx.coroutines.flow.Flow

interface IWeatherViewHistoryRepository {

    fun getAll(): Flow<List<WeatherViewHistoryEntity>>

    fun getByCityName(name: String): Flow<List<WeatherViewHistoryEntity>>

    suspend fun post(entity: WeatherViewHistoryEntity)

    suspend fun put(entity: WeatherViewHistoryEntity)

    suspend fun delete(entity: WeatherViewHistoryEntity)
}