package com.example.androidwithkotlin.api

import com.example.androidwithkotlin.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

//TODO Разобраться с API получения информации по городам, странам и часстям света
interface HtmlWebGeoApi {
    @GET("search")
    fun search(
        @Query("api_key")
        apiKey: String = BuildConfig.HTMLWEB_API_KEY,
        @Query("search")
        search: String
    )
}