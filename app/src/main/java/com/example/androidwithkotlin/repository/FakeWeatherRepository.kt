package com.example.androidwithkotlin.repository

import com.example.androidwithkotlin.model.City
import com.example.androidwithkotlin.model.Country
import com.example.androidwithkotlin.model.Weather

class FakeWeatherRepository : IWeatherRepository {
    override fun getAll(): List<Weather> {
        return allCities
    }

    override fun getByCity(city: City): Weather? {
        return allCities.firstOrNull { weather -> weather.city.city == city.city }
    }

    override fun getByCountry(country: Country): List<Weather> {
        return allCities.filter { weather -> weather.city.country.country == country.country }
    }
}


private val allCities = listOf(
    Weather(City("Лондон", 51.5085300, -0.1257400, Country("Другие", listOf())), 1, 2),
    Weather(City("Токио", 35.6895000, 139.6917100, Country("Другие", listOf())), 3, 4),
    Weather(City("Париж", 48.8534100, 2.3488000, Country("Другие", listOf())), 5, 6),
    Weather(City("Берлин", 52.52000659999999, 13.404953999999975, Country("Другие", listOf())), 7, 8),
    Weather(City("Рим", 41.9027835, 12.496365500000024, Country("Другие", listOf())), 9, 10),
    Weather(City("Минск", 53.90453979999999, 27.561524400000053, Country("Другие", listOf())), 11, 12),
    Weather(City("Стамбул", 41.0082376, 28.97835889999999, Country("Другие", listOf())), 13, 14),
    Weather(City("Вашингтон", 38.9071923, -77.03687070000001, Country("Другие", listOf())), 15, 16),
    Weather(City("Киев", 50.4501, 30.523400000000038, Country("Другие", listOf())), 17, 18),
    Weather(City("Пекин", 39.90419989999999, 116.40739630000007, Country("Другие", listOf())), 19, 20),
    Weather(City("Москва", 55.755826, 37.617299900000035, Country("Россия", listOf())), 1, 2),
    Weather(City("Санкт-Петербург", 59.9342802, 30.335098600000038, Country("Россия", listOf())), 3, 3),
    Weather(City("Новосибирск", 55.00835259999999, 82.93573270000002, Country("Россия", listOf())), 5, 6),
    Weather(City("Екатеринбург", 56.83892609999999, 60.60570250000001, Country("Россия", listOf())), 7, 8),
    Weather(City("Нижний Новгород", 56.2965039, 43.936059, Country("Россия", listOf())), 9, 10),
    Weather(City("Казань", 55.8304307, 49.06608060000008, Country("Россия", listOf())), 11, 12),
    Weather(City("Челябинск", 55.1644419, 61.4368432, Country("Россия", listOf())), 13, 14),
    Weather(City("Омск", 54.9884804, 73.32423610000001, Country("Россия", listOf())), 15, 16),
    Weather(City("Ростов-на-Дону", 47.2357137, 39.701505, Country("Россия", listOf())), 17, 18),
    Weather(City("Уфа", 54.7387621, 55.972055400000045, Country("Россия", listOf())), 19, 20)
)

//TODO Country repository