package com.jakey.sweaterweather.presentation

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jakey.sweaterweather.data.remote.responses.WeatherResponse
import com.jakey.sweaterweather.domain.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    private val _city = MutableLiveData<String>("Denver")
    val city: LiveData<String> get() = _city

    private val _weatherLiveData = MutableLiveData<WeatherResponse>()
    val weatherLiveData: LiveData<WeatherResponse> get() = _weatherLiveData

    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> get() = _loading

    fun setCity(city: String) = viewModelScope.launch {
        _city.postValue(city)
    }

    fun getWeather(city: String) = viewModelScope.launch {
        _loading.postValue(true)
        repository.getWeather(city).let { response ->
            Log.i("Response", response.toString())
            if (response.isSuccessful) {
                _loading.postValue(false)
                _weatherLiveData.postValue(response.body())
            } else {
                Log.d("Tag", "getWeather Error Response: ${response.message()}")
            }
        }
        _loading.postValue(false)
    }
}