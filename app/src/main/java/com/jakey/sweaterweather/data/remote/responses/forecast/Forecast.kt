package com.jakey.sweaterweather.data.remote.responses.forecast

data class Forecast(
    val forecastday: List<Forecastday> = listOf()
)