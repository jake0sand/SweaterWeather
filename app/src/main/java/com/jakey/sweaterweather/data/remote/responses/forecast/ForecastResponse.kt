package com.jakey.sweaterweather.data.remote.responses.forecast

import com.jakey.sweaterweather.domain.current_weather.WeatherLite

data class ForecastResponse(
    val current: Current = Current(),
    val forecast: Forecast = Forecast(),
    val location: Location = Location()
)

fun ForecastResponse.toWeather(): WeatherLite {
    return WeatherLite(
        tempF = current.temp_f.toInt(),
        windM = current.wind_mph.toInt(),
        conditionText = current.condition.text,
        conditionIcon = current.condition.icon
    )
}



