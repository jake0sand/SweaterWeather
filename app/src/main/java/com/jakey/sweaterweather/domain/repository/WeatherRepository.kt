package com.jakey.sweaterweather.domain.repository

import com.jakey.sweaterweather.data.remote.WeatherApi
import com.jakey.sweaterweather.data.remote.responses.forecast.toForecastLite
import com.jakey.sweaterweather.data.remote.responses.forecast.toWeather
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import com.jakey.sweaterweather.domain.current_weather.WeatherLite
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun getCurrentWeather(city: String?): WeatherLite {
        return city?.let { api.getCurrentWeather(city = it).body()?.toWeather() } ?: WeatherLite()
    }

    suspend fun getForecast(city: String): List<ForecastLite> {
        return api.getForecast(city = city)
            .body()?.forecast?.forecastday?.map { it.toForecastLite() }
            ?: listOf(ForecastLite())
    }
}


