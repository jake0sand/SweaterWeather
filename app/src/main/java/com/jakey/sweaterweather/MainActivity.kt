package com.jakey.sweaterweather

import android.app.Activity
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jakey.sweaterweather.data.DataStoreManager
import com.jakey.sweaterweather.databinding.ActivityMainBinding
import com.jakey.sweaterweather.presentation.WeatherAdapter
import com.jakey.sweaterweather.presentation.WeatherViewModel
import dagger.hilt.android.AndroidEntryPoint
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

    @Inject
    lateinit var dataStore: DataStoreManager


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


        if (timeOfDay >= 17 || timeOfDay < 5) binding.root.background = getDrawable(R.drawable.night_background)

        lifecycleScope.launchWhenStarted {

            viewModel.isLoading.collectLatest { isLoading ->
                binding.apply {
                    progressBar.isVisible = isLoading
                    if (isLoading) progressBar.playAnimation()
                    group.isVisible = !isLoading
                }
            }
        }


        binding.etLocation.apply {
            doOnTextChanged { text, start, before, count ->
                binding.icSearch.visibility = View.VISIBLE
                binding.icSearch.background = getDrawable(R.drawable.custom_image_buttom_green)
            }
        }

        binding.icLocation.setOnClickListener {
            saveLocationDialog()
        }

        binding.root.setOnClickListener { it.hideKeyboard() }

        lifecycleScope.launchWhenStarted {

            viewModel.weatherStateFlow.collectLatest { currentWeather ->
                binding.apply {
                    currentWeather.let {

                        tvCurrentTemp.text = "${currentWeather.tempF} °F"
                        tvWind.text = "${currentWeather.windM} mph"
                        tvDescription.text = currentWeather.conditionText
                        when {
                            currentWeather.conditionText.lowercase() == "clear" -> {
                                ivCurrentWeather.setAnimation(R.raw.day_night)
                            }
                            currentWeather.conditionText.contains("rain", true) -> {
                                ivCurrentWeather.setAnimation(R.raw.partly_shower)
                            }
                            currentWeather.conditionText.contains("cloud", true) -> {
                                ivCurrentWeather.setAnimation(R.raw.mostly_sunny)
                            }
                            currentWeather.conditionText.contains("snow", true) -> {
                                ivCurrentWeather.setAnimation(R.raw.snow_animation)
                            }
                        }
                        ivCurrentWeather.playAnimation()

                        icSearch.setOnClickListener {
                            if (binding.etLocation.text?.isNotBlank() == true) {
                                viewModel.getCurrentWeather(etLocation.text.toString())
                                viewModel.getForecast(etLocation.text.toString())
                            }

                            icSearch.background = getDrawable(R.drawable.custom_image_button)
                            icSearch.isVisible = false
                            icSearch.hideKeyboard()
                        }


                    }
                    imeSearchRequest(etLocation)
                }
            }
        }

        setupRv()
    }

    private fun imeSearchRequest(editText: EditText) {
        editText.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->

            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                // Do something of your interest.
                // We in this examples created the following Toasts
                if (editText.text.isNotBlank()) {
                    viewModel.getForecast(editText.text.toString())
                }
                editText.hideKeyboard()
                binding.icSearch.isVisible = false
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun View.hideKeyboard(): Boolean {
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
            viewModel.forecastStateFlow.collectLatest { forecastList ->

                forecastList[0].date = "Today"
                weatherAdapter.forecastList = forecastList
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