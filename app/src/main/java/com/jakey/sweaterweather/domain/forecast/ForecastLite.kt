package com.jakey.sweaterweather.domain.forecast

data class ForecastLite(
    val tempF: String? = null,
    val windM: String? = null,
    val conditionText: String? = null,
    val conditionIcon: String? = null,
    var date: String? = null
)
