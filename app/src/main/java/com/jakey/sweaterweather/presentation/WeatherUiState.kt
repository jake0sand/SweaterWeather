package com.jakey.sweaterweather.presentation

import com.jakey.sweaterweather.domain.current_weather.WeatherLite

data class WeatherUiState(
    val weatherLite: WeatherLite? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)