package com.example.androidwithkotlin.db.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class WeatherViewHistoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val city: String,
    val temperature: Int,
    val feelsLike: Int,
    //TODO Room date adapter
    val addDate: Long = Calendar.getInstance().timeInMillis
)