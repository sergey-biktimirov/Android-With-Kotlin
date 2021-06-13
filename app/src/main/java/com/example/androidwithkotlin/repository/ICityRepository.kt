package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.model.City

interface ICityRepository {
    suspend fun getAll(): List<City>
    suspend fun getByCountry(country: String): List<City>
    suspend fun getByName(city: String): City?
}