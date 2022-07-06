package com.jakey.sweaterweather.domain.forecast

data class ForecastLite(
    val tempF: Int = 0,
    val windM: Int = 0,
    val conditionText: String = "",
    val conditionIcon: String = "",
    val date: String = ""
)
