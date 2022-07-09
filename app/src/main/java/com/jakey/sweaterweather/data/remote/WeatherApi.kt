package com.jakey.sweaterweather.data.remote

import com.jakey.sweaterweather.data.remote.responses.forecast.ForecastResponse
import com.jakey.sweaterweather.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("current.json")
    suspend fun getCurrentWeather(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") city: String,
        @Query("aqi") airQuality: String = "no"
    ): ForecastResponse

    @GET("forecast.json")
    suspend fun getForecast(
        @Query("key") apiKey: String = BuildConfig.API_KEY,
        @Query("q") city: String,
        @Query("aqi") airQuality: String = "no",
        @Query("days") days: Int = 3,
        @Query("alerts") alerts: String = "no"
    ): ForecastResponse
}