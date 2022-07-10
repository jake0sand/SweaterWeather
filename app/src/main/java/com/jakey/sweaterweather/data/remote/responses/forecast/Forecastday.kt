package com.jakey.sweaterweather.data.remote.responses.forecast

import android.util.Log
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import java.text.SimpleDateFormat
import java.util.*

data class Forecastday(
    val astro: Astro = Astro(),
    val date: String = "",
    val date_epoch: Long = 0,
    val day: Day = Day(),
    val hour: List<Hour> = listOf()
)

fun Forecastday.toForecastLite(): ForecastLite {
    return ForecastLite(
        tempF = day.maxtemp_f.toInt().toString(),
        windM = day.maxwind_mph.toInt().toString(),
        conditionIcon = "https:${day.condition.icon}",
        conditionText = day.condition.text,
        //I have no idea why this is coming in one day behind so I just added 12 hours to the value
        date = sdf.format(date_epoch * 1000 + 43200000)
    )

}

private val sdf = SimpleDateFormat("EEE", Locale.US)




