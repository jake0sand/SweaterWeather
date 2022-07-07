package com.jakey.sweaterweather.domain.forecast

data class ForecastLite(
    val tempF: String = "",
    val windM: String = "",
    val conditionText: String = "",
    val conditionIcon: String = "",
    val date: String = ""
)
