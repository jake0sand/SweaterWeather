package com.jakey.sweaterweather.data.remote

import com.jakey.sweaterweather.data.remote.responses.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherApiService {

    @GET("weather/{city}")
    suspend fun getWeatherByCity(
        @Path("city") city: String
    ): Response<WeatherResponse>
}