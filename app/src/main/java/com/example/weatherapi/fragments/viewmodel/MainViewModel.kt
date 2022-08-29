package com.example.weatherapi.fragments.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapi.data.WeatherDayItem

class MainViewModel : ViewModel() {
    val liveDataCurrent = MutableLiveData<WeatherDayItem>()
    val liveDataList = MutableLiveData<List<WeatherDayItem>>()
}