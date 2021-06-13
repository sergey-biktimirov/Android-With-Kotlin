package com.example.androidwithkotlin.api

import com.example.androidwithkotlin.BuildConfig
import com.example.androidwithkotlin.api.retrofit.DefaultInterceptor
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonRootName
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface YandexWeatherAPI {
    @Headers("X-Yandex-API-Key:${BuildConfig.YANDEX_WEATHER_API_KEY}")
    @GET(BuildConfig.YANDEX_WEATHER_API_ENDPOINT)
    fun get(
        @Query("lat") lat: Float,
        @Query("lon") lon: Float,
        @Query("lang") lang: String = "ru_RU",
        @Query("limit") limit: Int = 7,
        @Query("hours") hours: Boolean = false,
        @Query("extra") extra: Boolean = false
    ): Call<YandexWeatherDTO>

    companion object {
        val retrofitService: YandexWeatherAPI
            get() {
                val jacksonObjectMapper = ObjectMapper()
                jacksonObjectMapper
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

                return Retrofit.Builder()
                    .baseUrl(BuildConfig.YANDEX_WEATHER_API_BASE_URL)
                    .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper))
                    .client(
                        OkHttpClient
                            .Builder()
                            .addNetworkInterceptor(DefaultInterceptor())
                            .build()
                    )
                    .build()
                    .create(YandexWeatherAPI::class.java)
            }
    }
}

/**
 * @param now Время сервера в формате Unixtime.
 * @param nowDt Время сервера в UTC.
 * @param info Объект информации о населенном пункте.
 * @param fact Объект фактической информации о погоде.
 * @param forecast Объект прогнозной информации о погоде.
 * */
data class YandexWeatherDTO(
    @JsonProperty("now")
    var now: Long? = null,
    @JsonProperty("now_dt")
    var nowDt: String? = null,
    @JsonProperty("info")
    var info: YandexWeatherInfoDTO? = null,
    @JsonProperty("fact")
    var fact: YandexWeatherFactDTO? = null,
    @JsonProperty("forecasts")
    var forecast: List<YandexWeatherForecastDTO>? = null
)

/** Объект info
 * Объект содержит информацию о населенном пункте.
 * @param lat Широта (в градусах).
 * @param lon Долгота (в градусах).
 * @param tzInfo Информация о часовом поясе. Содержит поля offset, name, abbr и dst.
 * @param offset Часовой пояс в секундах от UTC.
 * @param name Название часового пояса.
 * @param abbr Сокращенное название часового пояса.
 * @param dst Признак летнего времени.
 * @param defPressureMm Норма давления для данной координаты (в мм рт. ст.).
 * @param defPressurePa Норма давления для данной координаты (в гектопаскалях).
 * @param url Страница населенного пункта на сайте Яндекс.Погода.[https://yandex.ru/pogoda/]
 * */
@JsonRootName("info")
data class YandexWeatherInfoDTO(
    @JsonProperty("lat")
    var lat: Float? = null,
    @JsonProperty("lon")
    var lon: Float? = null,
    @JsonProperty("tzinfo")
    var tzInfo: YandexWeatherTZInfoDTO? = null,
    @JsonProperty("offset")
    var offset: Long? = null,
    @JsonProperty("name")
    var name: String? = null,
    @JsonProperty("abbr")
    var abbr: String? = null,
    @JsonProperty("dst")
    var dst: Boolean? = null,
    @JsonProperty("def_pressure_mm")
    var defPressureMm: Int? = null,
    @JsonProperty("def_pressure_pa")
    var defPressurePa: Int? = null,
    @JsonProperty("url")
    var url: String? = null
)

/** Объект содержит информацию о погоде на данный момент.
 * @param temp Температура (°C).
 * @param feelsLike Ощущаемая температура (°C).
 * @param tempWater Температура воды (°C). Параметр возвращается для населенных пунктов, где данная
 * информация актуальна.
 * @param icon Код иконки погоды. Иконка доступна по адресу
 * https://yastatic.net/weather/i/icons/blueye/color/svg/<значение из поля icon>.svg.
 * @param condition Код расшифровки погодного описания. Возможные значения:
 * @param pressureMm Давление (в мм рт. ст.).
 * @param feelsLike
 * */
//TODO доделать описания параметров класса
// https://yandex.ru/dev/weather/doc/dg/concepts/forecast-test.html#resp-format__fact
@JsonRootName("fact")
data class YandexWeatherFactDTO(
    @JsonProperty("temp")
    var temp: Int? = null,
    @JsonProperty("feels_like")
    var feelsLike: Int? = null,
    @JsonProperty("temp_water")
    var tempWater: Int? = null,
    @JsonProperty("icon")
    var icon: String? = null,
    @JsonProperty("condition")
    var condition: String? = null,
    @JsonProperty("wind_speed")
    var windSpeed: Int? = null,
    @JsonProperty("wind_gust")
    var windGust: Int? = null,
    @JsonProperty("wind_dir")
    var windDir: String? = null,
    @JsonProperty("pressure_mm")
    var pressureMm: Int? = null,
    @JsonProperty("pressure_pa")
    var pressurePa: Int? = null,
    @JsonProperty("humidity")
    var humidity: Int? = null,
    @JsonProperty("daytime")
    var dayTime: String? = null,
    @JsonProperty("polar")
    var polar: Boolean? = null,
    @JsonProperty("season")
    var season: String? = null,
    @JsonProperty("obs_time")
    var obsTime: Long? = null,
    @JsonProperty("is_thunder")
    var isThunder: Boolean? = null,
    @JsonProperty("prec_type")
    var precType: Int? = null,
    @JsonProperty("prec_strength")
    var precStrength: Int? = null,
    @JsonProperty("cloudness")
    var cloudness: Float? = null,
    @JsonProperty("phenom_icon")
    var phenomIcon: String? = null,
    @JsonProperty("phenom_condition")
    var phenomCondition: String? = null
)

@JsonRootName("forecast")
data class YandexWeatherForecastDTO(
    @JsonProperty("date")
    var date: String? = null,
    @JsonProperty("date_ts")
    var dateTs: Long? = null,
    @JsonProperty("week")
    var week: Int? = null,
    @JsonProperty("sunrise")
    var sunrise: String? = null,
    @JsonProperty("sunset")
    var sunset: String? = null,
    @JsonProperty("moon_code")
    var moonCode: Int? = null,
    @JsonProperty("moon_text")
    var moonText: String? = null,
//    @JsonProperty("parts")
//    var parts:? = null
//    @JsonProperty("night")
//    var night:? = null
    @JsonProperty("temp_min")
    var tempMin: Int? = null,
    @JsonProperty("temp_max")
    var tempMax: Int? = null,
    @JsonProperty("temp_avg")
    var tempAvg: Int? = null,
    @JsonProperty("feels_like")
    var feelsLike: Int? = null,
    @JsonProperty("icon")
    var icon: String? = null,
    @JsonProperty("condition")
    var condition: String? = null,
    @JsonProperty("daytime")
    var dateTime: String? = null,
    @JsonProperty("polar")
    var polar: Boolean? = null,
    @JsonProperty("wind_speed")
    var windSpeed: Int? = null,
    @JsonProperty("wind_gust")
    var windGust: Int? = null,
    @JsonProperty("wind_dir")
    var windDir: String? = null,
    @JsonProperty("pressure_mm")
    var pressureMm: Int? = null,
    @JsonProperty("pressure_pa")
    var pressurePa: Int? = null,
    @JsonProperty("humidity")
    var humidity: Int? = null,
    @JsonProperty("prec_mm")
    var precMm: Int? = null,
    @JsonProperty("prec_period")
    var precPeriod: Int? = null,
    @JsonProperty("prec_type")
    var precType: Int? = null,
    @JsonProperty("prec_strength")
    var precStrength: Int? = null,
    @JsonProperty("cloudness")
    var cloudness: Float? = null,
//    @JsonProperty("day_short")
//    var dayShort:? = null
    @JsonProperty("temp")
    var temp: Int? = null,
//    @JsonProperty("hours")
    //var hours:? = null
    @JsonProperty("hour")
    var hour: String? = null,
    @JsonProperty("hour_ts")
    var hourTs: Long? = null
)

@JsonRootName("tzinfo")
data class YandexWeatherTZInfoDTO(
    @JsonProperty("offset")
    var offset: Long? = null,
    @JsonProperty("name")
    var name: String? = null,
    @JsonProperty("abbr")
    var abbr: String? = null,
    @JsonProperty("dst")
    var dst: Boolean? = null
)