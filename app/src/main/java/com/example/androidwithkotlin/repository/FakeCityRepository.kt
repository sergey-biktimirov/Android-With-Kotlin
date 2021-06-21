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
        City("Лондон", 51.5085300, -0.1257400, Country("Другие", listOf())),
        City("Токио", 35.6895000, 139.6917100, Country("Другие", listOf())),
        City("Париж", 48.8534100, 2.3488000, Country("Другие", listOf())),
        City("Берлин", 52.52000659999999, 13.404953999999975, Country("Другие", listOf())),
        City("Рим", 41.9027835, 12.496365500000024, Country("Другие", listOf())),
        City("Минск", 53.90453979999999, 27.561524400000053, Country("Другие", listOf())),
        City("Стамбул", 41.0082376, 28.97835889999999, Country("Другие", listOf())),
        City("Вашингтон", 38.9071923, -77.03687070000001, Country("Другие", listOf())),
        City("Киев", 50.4501, 30.523400000000038, Country("Другие", listOf())),
        City("Пекин", 39.90419989999999, 116.40739630000007, Country("Другие", listOf())),
        City("Москва", 55.755826, 37.617299900000035, Country("Россия", listOf())),
        City("Санкт-Петербург", 59.9342802, 30.335098600000038, Country("Россия", listOf())),
        City("Новосибирск", 55.00835259999999, 82.93573270000002, Country("Россия", listOf())),
        City("Екатеринбург", 56.83892609999999, 60.60570250000001, Country("Россия", listOf())),
        City("Нижний Новгород", 56.2965039, 43.936059, Country("Россия", listOf())),
        City("Казань", 55.8304307, 49.06608060000008, Country("Россия", listOf())),
        City("Челябинск", 55.1644419, 61.4368432, Country("Россия", listOf())),
        City("Омск", 54.9884804, 73.32423610000001, Country("Россия", listOf())),
        City("Ростов-на-Дону", 47.2357137, 39.701505, Country("Россия", listOf())),
        City("Уфа", 54.7387621, 55.972055400000045, Country("Россия", listOf()))
    )
}