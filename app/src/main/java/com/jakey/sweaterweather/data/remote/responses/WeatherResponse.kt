package com.jakey.sweaterweather.data.remote.responses

import com.jakey.sweaterweather.domain.model.Weather
import com.jakey.sweaterweather.utils.Helper.celToFahr
import com.jakey.sweaterweather.utils.Helper.kphToMph

data class WeatherResponse(
    val description: String = "",
    val forecast: List<ForecastResponse> = listOf(),
    val temperature: String = "",
    val wind: String = ""
)

fun WeatherResponse.toWeather(): Weather {
    return Weather(
        description,
        forecast,
        temperatureF = temperature.celToFahr(),
        temperatureC = temperature,
        windM = wind.kphToMph(),
        windK = wind
    )
}
