package com.jakey.sweaterweather.domain.repository

import android.util.Log
import com.jakey.sweaterweather.data.remote.WeatherApi
import com.jakey.sweaterweather.data.remote.responses.forecast.toForecastLite
import com.jakey.sweaterweather.data.remote.responses.forecast.toWeather
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import com.jakey.sweaterweather.domain.current_weather.WeatherLite
import com.jakey.sweaterweather.utils.Resource
import retrofit2.HttpException
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val api: WeatherApi
) {

    suspend fun getCurrentWeather(city: String): Resource<WeatherLite> {
        return try {
            Resource.Success(
                data = api.getCurrentWeather(city = city).toWeather()
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }

    suspend fun getForecast(city: String): Resource<List<ForecastLite>> {
        return try {
            Resource.Success(
                data = api.getForecast(city = city).forecast.forecastday.map { it.toForecastLite() }
            )
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An unknown error occurred.")
        }
    }
}


