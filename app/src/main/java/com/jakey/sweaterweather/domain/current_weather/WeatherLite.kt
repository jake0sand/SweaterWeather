package com.jakey.sweaterweather.domain.current_weather

data class WeatherLite(
    val tempF: Int = 0,
    val windM: Int = 0,
    val conditionText: String = "",
    val conditionIcon: String = ""
)
