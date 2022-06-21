package com.jakey.sweaterweather.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jakey.sweaterweather.data.remote.responses.ForecastResponse
import com.jakey.sweaterweather.data.remote.responses.toForecast
import com.jakey.sweaterweather.databinding.ForecastCardItemBinding

class ForecastAdapter(
    val weatherList: List<ForecastResponse>
) : RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    inner class ForecastViewHolder(val binding: ForecastCardItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        return ForecastViewHolder(
            ForecastCardItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {

        val currentItem = weatherList[position].toForecast()

//        holder.binding.ivForecast
        holder.binding.apply {
            tvTempForecast.text = currentItem.tempF.toString()
            tvWindForecast.text = currentItem.windM.toString()
        }

    }

    override fun getItemCount(): Int {
        return weatherList.size
    }

}