package com.example.androidwithkotlin.db.room.dao

import androidx.room.*
import com.example.androidwithkotlin.db.room.entity.WeatherViewHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IWeatherViewHistoryDAO {

    @Query("select * from WeatherViewHistoryEntity order by addDate desc, city")
    fun selectAll(): Flow<List<WeatherViewHistoryEntity>>

    @Query("select * from WeatherViewHistoryEntity where city like :name order by addDate desc")
    fun selectByCityName(name: String): Flow<List<WeatherViewHistoryEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(entity: WeatherViewHistoryEntity)

    @Update
    suspend fun update(entity: WeatherViewHistoryEntity)

    @Delete
    suspend fun delete(entity: WeatherViewHistoryEntity)
}