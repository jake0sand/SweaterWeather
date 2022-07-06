package com.jakey.sweaterweather.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakey.sweaterweather.domain.current_weather.WeatherLite
import com.jakey.sweaterweather.domain.repository.WeatherRepository
import com.jakey.sweaterweather.data.DataStoreManager
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    private val _weatherStateFlow = MutableStateFlow(WeatherLite())
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    private val _forecastStateFlow = MutableStateFlow(listOf(ForecastLite()))
    val forecastStateFlow = _forecastStateFlow.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentWeather(city = dataStore.readLocation())
            getForecast(city = dataStore.readLocation())
        }
    }

    fun getForecast(city: String) {
        viewModelScope.launch {
            val response = repository.getForecast(city)
            _forecastStateFlow.value = response
        }
    }

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            val response = repository.getCurrentWeather(city)
            _weatherStateFlow.value = response
        }
    }

}