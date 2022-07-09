package com.jakey.sweaterweather.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakey.sweaterweather.domain.current_weather.WeatherLite
import com.jakey.sweaterweather.domain.repository.WeatherRepository
import com.jakey.sweaterweather.data.DataStoreManager
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import com.jakey.sweaterweather.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository,
    private val dataStore: DataStoreManager
): ViewModel() {

    private val _weatherStateFlow = MutableStateFlow(WeatherLite())
    val weatherStateFlow = _weatherStateFlow.asStateFlow()

    private val _forecastStateFlow = MutableStateFlow(listOf(ForecastLite(), ForecastLite(), ForecastLite()))
    val forecastStateFlow = _forecastStateFlow.asStateFlow()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            getCurrentWeather(city = dataStore.readLocation())
            getForecast(city = dataStore.readLocation())
        }
    }

    fun setLoading(bool: Boolean) {
        viewModelScope.launch {
            _isLoading.value = bool
        }
    }

    fun getCurrentWeather(city: String) {
        viewModelScope.launch {
            val result = repository.getCurrentWeather(city)
            _isLoading.value = true
            _weatherStateFlow.value = result.data ?: WeatherLite()
            _isLoading.value = false
        }
    }

    fun getForecast(city: String) {
        viewModelScope.launch {
            _isLoading.value = true
            val response = repository.getForecast(city)
            _forecastStateFlow.value = response.data ?: listOf(ForecastLite())
            _isLoading.value = false
        }
    }



}