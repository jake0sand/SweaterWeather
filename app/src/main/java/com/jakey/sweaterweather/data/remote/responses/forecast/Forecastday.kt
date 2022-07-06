package com.jakey.sweaterweather.data.remote.responses.forecast

import com.jakey.sweaterweather.domain.forecast.ForecastLite

data class Forecastday(
    val astro: Astro = Astro(),
    val date: String = "",
    val date_epoch: Int = 0,
    val day: Day = Day(),
    val hour: List<Hour> = listOf()
)

fun Forecastday.toForecastLite(): ForecastLite {
    return ForecastLite(
        tempF = day.avgtemp_f.toInt(),
        windM = day.maxwind_mph.toInt(),
        conditionIcon = "https:${day.condition.icon}",
        conditionText = day.condition.text,
        date = date
    )
}