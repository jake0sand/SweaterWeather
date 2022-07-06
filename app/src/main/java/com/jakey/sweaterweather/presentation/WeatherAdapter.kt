package com.jakey.sweaterweather.presentation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.jakey.sweaterweather.databinding.ForecastCardItemBinding
import com.jakey.sweaterweather.domain.forecast.ForecastLite
import java.text.SimpleDateFormat

class WeatherAdapter : RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder>() {

    inner class WeatherViewHolder(val binding: ForecastCardItemBinding) :
            RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ForecastLite>() {
        override fun areItemsTheSame(oldItem: ForecastLite, newItem: ForecastLite): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: ForecastLite, newItem: ForecastLite): Boolean {
            return newItem == oldItem
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)
    var forecastList: List<ForecastLite>
        get() = differ.currentList
        set(value) {
            differ.submitList(value)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        return WeatherViewHolder(ForecastCardItemBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        ))
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        val currentForecast = forecastList[position]

        holder.binding.apply {
            tvWindForecast.text = currentForecast.windM.toString()
            tvTempForecast.text = "HI   ${currentForecast.tempF}"
            ivForecast.load(currentForecast.conditionIcon)
            tvDay.text = currentForecast.date
        }
    }

    override fun getItemCount(): Int {
        return forecastList.size
    }
}