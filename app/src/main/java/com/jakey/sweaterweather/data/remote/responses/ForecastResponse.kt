package com.jakey.sweaterweather.data.remote.responses

import com.jakey.sweaterweather.domain.model.Forecast
import com.jakey.sweaterweather.utils.Helper.celToFahr
import com.jakey.sweaterweather.utils.Helper.kphToMph


data class ForecastResponse(
    val day: String = "",
    val temperature: String = "",
    val wind: String = ""
)

fun ForecastResponse.toForecast(): Forecast {
    return Forecast(
        tempF =  temperature.celToFahr(),
        tempC = temperature,
        windK = wind,
        windM = wind.kphToMph(),
        description = "",
        day = day
    )
}

