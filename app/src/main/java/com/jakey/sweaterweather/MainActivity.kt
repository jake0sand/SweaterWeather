package com.jakey.sweaterweather

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.jakey.sweaterweather.data.DataStoreManager
import com.jakey.sweaterweather.data.remote.WeatherApi
import com.jakey.sweaterweather.databinding.ActivityMainBinding
import com.jakey.sweaterweather.presentation.WeatherAdapter
import com.jakey.sweaterweather.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private lateinit var weatherAdapter: WeatherAdapter
    @Inject lateinit var dataStore: DataStoreManager


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


        if (timeOfDay >= 17) binding.root.background = getDrawable(R.drawable.night_background)

        lifecycleScope.launchWhenStarted {

            viewModel.isLoading.collectLatest { isLoading ->
                binding.apply {
                    progressBar.isVisible = isLoading
                    if (isLoading) progressBar.playAnimation()
                    group.isVisible = !isLoading
                }
            }
        }

        lifecycleScope.launchWhenStarted {

            viewModel.weatherStateFlow.collectLatest { currentWeather ->
                binding.apply {
                    tvCurrentTemp.text = "${currentWeather.tempF} °F"
                    tvWind.text = "${currentWeather.windM} mph"
                    tvDescription.text = currentWeather.conditionText
//                    if (currentWeather.conditionText.contains("rain".lowercase())) {
//                        animCurrentWeather.setAnimation(R.raw.partly_shower)
//                    } else {
//                        animCurrentWeather.setAnimation(R.raw.mostly_sunny)
//                    }
                    ivCurrentWeather.load(currentWeather.conditionIcon)
                    icLocation.setOnClickListener {
                        saveLocationDialog()
                    }
                    icSearch.setOnClickListener {
                        viewModel.getCurrentWeather(etLocation.text.toString())
                        viewModel.getForecast(etLocation.text.toString())
                        icSearch.hideKeyboard()
                    }
                }


            }
            viewModel.forecastStateFlow.collectLatest {
                Snackbar.make(binding.root, "${it.map { it.tempF }}", Snackbar.LENGTH_INDEFINITE).show()
            }
        }

        setupRv()
    }

    private fun ImageView.hideKeyboard(): Boolean {
        return (context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager)
            .hideSoftInputFromWindow(windowToken, 0)
    }

    private fun setupRv() {
        weatherAdapter = WeatherAdapter()

        binding.recyclerview.apply {
            adapter = weatherAdapter
            layoutManager = LinearLayoutManager(
                this@MainActivity, LinearLayoutManager.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }
        lifecycleScope.launchWhenStarted {
            viewModel.forecastStateFlow.collectLatest {
                weatherAdapter.forecastList = it
            }
        }
    }

    private fun saveLocationDialog() {
        val dialogBuilder =
            AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogView = inflater.inflate(R.layout.edit_text_custom_dialog, null)
        dialogBuilder.setView(dialogView)
        val editText: EditText = dialogView.findViewById(R.id.edit_text_1)

        dialogBuilder.apply {
            setTitle("Set default location")
            lifecycleScope.launch { editText.setText(dataStore.readLocation()) }
            setPositiveButton("Save") { _, _ ->
                lifecycleScope.launch {
                    binding
                    dataStore.saveLocation(value = editText.text.toString())

                    Snackbar.make(binding.root, "Default Location Saved", Snackbar.LENGTH_SHORT)
                        .show()
                }

            }
            setNegativeButton(
                "Cancel"
            ) { _, _ ->

            }.show()
        }
    }

}