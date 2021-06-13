package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country

class FakeCityRepository : ICityRepository {
    override suspend fun getAll(): List<City> {
        return allCities
    }

    override suspend fun getByCountry(country: String): List<City> {
        return allCities.filter { it.country.country == country }
    }

    override suspend fun getByName(city: String): City? {
        return allCities.firstOrNull() { it.city == city }
    }

    private val allCities = listOf(
        City("Лондон", 51.5085300f, -0.1257400f, Country("Другие", listOf())),
        City("Токио", 35.6895000f, 139.6917100f, Country("Другие", listOf())),
        City("Париж", 48.8534100f, 2.3488000f, Country("Другие", listOf())),
        City("Берлин", 52.52000659999999f, 13.404953999999975f, Country("Другие", listOf())),
        City("Рим", 41.9027835f, 12.496365500000024f, Country("Другие", listOf())),
        City("Минск", 53.90453979999999f, 27.561524400000053f, Country("Другие", listOf())),
        City("Стамбул", 41.0082376f, 28.97835889999999f, Country("Другие", listOf())),
        City("Вашингтон", 38.9071923f, -77.03687070000001f, Country("Другие", listOf())),
        City("Киев", 50.4501f, 30.523400000000038f, Country("Другие", listOf())),
        City("Пекин", 39.90419989999999f, 116.40739630000007f, Country("Другие", listOf())),
        City("Москва", 55.755826f, 37.617299900000035f, Country("Россия", listOf())),
        City("Санкт-Петербург", 59.9342802f, 30.335098600000038f, Country("Россия", listOf())),
        City("Новосибирск", 55.00835259999999f, 82.93573270000002f, Country("Россия", listOf())),
        City("Екатеринбург", 56.83892609999999f, 60.60570250000001f, Country("Россия", listOf())),
        City("Нижний Новгород", 56.2965039f, 43.936059f, Country("Россия", listOf())),
        City("Казань", 55.8304307f, 49.06608060000008f, Country("Россия", listOf())),
        City("Челябинск", 55.1644419f, 61.4368432f, Country("Россия", listOf())),
        City("Омск", 54.9884804f, 73.32423610000001f, Country("Россия", listOf())),
        City("Ростов-на-Дону", 47.2357137f, 39.701505f, Country("Россия", listOf())),
        City("Уфа", 54.7387621f, 55.972055400000045f, Country("Россия", listOf()))
    )
}