package com.jakey.sweaterweather

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import com.jakey.sweaterweather.data.remote.responses.toWeather
import com.jakey.sweaterweather.databinding.ActivityMainBinding
import com.jakey.sweaterweather.presentation.ForecastAdapter
import com.jakey.sweaterweather.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val windowInsetsController =
            ViewCompat.getWindowInsetsController(window.decorView)
        windowInsetsController?.isAppearanceLightNavigationBars = true


        val date = Date()
        val formatter = SimpleDateFormat("HH")
        val timeOfDay = (formatter.format(date)).toInt()
        Log.d("Date", "TIME: $timeOfDay")

        val daysMap: Map<Int, String> = mapOf(
            0 to "Sunday",
            1 to "Monday",
            2 to "Tuesday",
            3 to "Wednesday",
            4 to "Thursday",
            5 to "Friday",
            6 to "Saturday",
        )

        if (timeOfDay >= 17) binding.root.background = getDrawable(R.drawable.night_background)







        viewModel.loading.observe(this) { loading ->
            binding.apply {
                animCurrentWeather.isVisible = !loading
                tvCurrentTemp.isVisible = !loading
                tvDescription.isVisible = !loading
                animWind.isVisible = !loading
                tvWind.isVisible = !loading
                lottieAnim.isVisible = loading
                recyclerview.isVisible = !loading

                if (loading) lottieAnim.playAnimation()
            }

        }

        viewModel.city.observe(this) {
            binding.tvLocation.text = it
            viewModel.getWeather(it)
        }


        binding.icEditLocation.setOnClickListener {
            binding.apply {
                icEditLocation.isVisible = false
                tvLocation.visibility = View.INVISIBLE
                etLocation.isVisible = true
                icCheck.isVisible = true
                icCheck.setOnClickListener {
                    binding.apply {
                        etLocation.isVisible = false
                        icCheck.isVisible = false
                        viewModel.setCity(etLocation.text.toString())
                        tvLocation.isVisible = true
                        icEditLocation.isVisible = true
                        etLocation.hideKeyboard()
                    }
                }
            }
        }

        viewModel.weatherLiveData.observe(this) { weatherResponse ->

            binding.apply {
                tvCurrentTemp.text = (weatherResponse.toWeather().temperatureF)
                tvCurrentTemp.setOnClickListener {
                    if (tvCurrentTemp.text.contains('F')) {
                        tvCurrentTemp.text = (weatherResponse.toWeather().temperatureC)
                    } else if (tvCurrentTemp.text.contains('C')) {
                        tvCurrentTemp.text = (weatherResponse.toWeather().temperatureF)
                    }
                    if (tvWind.text.contains('k')) {
                        tvWind.text = weatherResponse.toWeather().windM
                    } else if (tvWind.text.contains('p')) {
                        tvWind.text = weatherResponse.toWeather().windK
                    }
                }
                tvDescription.text = weatherResponse.toWeather().description
                tvWind.text = weatherResponse.toWeather().windM
            }
            forecastAdapter = ForecastAdapter(weatherList = weatherResponse.forecast)
            Log.i("weatherlist", "${weatherResponse.forecast}")
            binding.recyclerview.apply {
                adapter = forecastAdapter
                setHasFixedSize(true)
            }
        }
    }

    private fun EditText.hideKeyboard(): Boolean {
        return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
    }
}