package com.example.weatherapi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapi.R
import com.example.weatherapi.data.WeatherDayItem
import com.example.weatherapi.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class WeatherAdapter : ListAdapter<WeatherDayItem,WeatherAdapter.WeatherHolder>(Comparator()) {

    class WeatherHolder(  view : View) : RecyclerView.ViewHolder(view) {

        private val b = ListItemBinding.bind(view)

        fun setWeatherData(item : WeatherDayItem) = with(b){
            tvDate.text = item.date

            tvCondition.text = item.condition

            tvTemp.text = item.currentTemp.ifEmpty { "${item.maxTemp}℃ / ${item.minTemp}℃"  }

            Picasso.get().load("https:${item.image}").into(imw)


        }

    }

    class Comparator : DiffUtil.ItemCallback<WeatherDayItem>() {
        override fun areItemsTheSame(oldItem: WeatherDayItem, newItem: WeatherDayItem): Boolean {
           return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: WeatherDayItem, newItem: WeatherDayItem): Boolean {
           return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item,parent,false)
        return WeatherHolder(view)
    }

    override fun onBindViewHolder(holder: WeatherHolder, position: Int) {
        holder.setWeatherData(getItem(position))
    }
}