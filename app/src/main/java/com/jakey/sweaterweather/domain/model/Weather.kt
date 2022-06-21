package com.jakey.sweaterweather.domain.model

import com.jakey.sweaterweather.data.remote.responses.ForecastResponse

data class Weather(
    val description: String = "",
    val forecast: List<ForecastResponse> = listOf(),
    val temperatureC: String = "",
    val temperatureF: String = "",
    val windM: String = "",
    val windK: String = ""
)


