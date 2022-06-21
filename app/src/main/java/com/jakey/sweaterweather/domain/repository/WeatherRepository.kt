package com.jakey.sweaterweather.domain.repository

import com.jakey.sweaterweather.data.remote.WeatherApiService
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApiService
) {

    suspend fun getWeather(city: String) = api.getWeatherByCity(city)
}