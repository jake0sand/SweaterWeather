package com.jakey.sweaterweather.domain.model

data class Forecast(
    val day: String,
    val description: String? = null,
    val windK: Any,
    val tempC: Any,
    val tempF: String,
    val windM: String
)
