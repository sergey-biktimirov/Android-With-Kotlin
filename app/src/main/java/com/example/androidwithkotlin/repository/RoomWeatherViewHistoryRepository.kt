package com.example.androidwithkotlin.repository

import androidx.annotation.WorkerThread
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import com.example.androidwithkotlin.extension.roomDataBase
import kotlinx.coroutines.flow.Flow

class RoomWeatherViewHistoryRepository : IWeatherViewHistoryRepository {

    private val weatherViewHistoryDAO = roomDataBase.weatherViewHistoryDAO()

    override fun getAll(): Flow<List<WeatherViewHistoryEntity>> {
        return weatherViewHistoryDAO.selectAll()
    }

    override fun getByCityName(name: String): Flow<List<WeatherViewHistoryEntity>> {
        return weatherViewHistoryDAO.selectByCityName(name)
    }

    @WorkerThread
    override suspend fun post(entity: WeatherViewHistoryEntity) {
        weatherViewHistoryDAO.insert(entity)
    }

    @WorkerThread
    override suspend fun put(entity: WeatherViewHistoryEntity) {
        weatherViewHistoryDAO.update(entity)
    }

    @WorkerThread
    override suspend fun delete(entity: WeatherViewHistoryEntity) {
        weatherViewHistoryDAO.delete(entity)
    }
}