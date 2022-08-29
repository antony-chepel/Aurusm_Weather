package com.example.weatherapi.fragments

import android.Manifest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.weatherapi.adapter.VpAdapter
import com.example.weatherapi.data.WeatherDayItem
import com.example.weatherapi.databinding.FragmentMainBinding
import com.example.weatherapi.fragments.viewmodel.MainViewModel
import com.example.weatherapi.utils.isPermissionsGranted
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import org.json.JSONObject

const val API_KEY = "aa7a0fc2128148ae931154725221206"

class MainFragment : Fragment() {
    private lateinit var activityLauncher: ActivityResultLauncher<String> // Благодаря лаунчера запускается запрос на разрешения
    private lateinit var binding: FragmentMainBinding
    val mainViewModel: MainViewModel by activityViewModels()
    val fraglist = listOf<Fragment>(HoursFragment.newInstance(), DaysFragment.newInstance())
    private val tabNames = listOf<String>("Hours", "Days")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkpermission()
        init()
        requestWeatherData("London")
        updateCurrentCard()
    }


    private fun init() = with(binding) {
        val adapter = VpAdapter(requireActivity(), fraglist)
        vpWeather.adapter = adapter
        TabLayoutMediator(tabLayout, vpWeather) { tab, pos ->
            tab.text = tabNames[pos]

        }.attach()

    }


    private fun updateCurrentCard() = with(binding){
        mainViewModel.liveDataCurrent.observe(viewLifecycleOwner, {
            tvCity.text = it.city
            tvDateTime.text = it.date
            tvTemp.text = "${it.currentTemp}℃"
            tvMinMaxTemp.text = "${it.minTemp}/${it.maxTemp}℃"
            tvWeatherDesc.text = it.condition
            Picasso.get().load("https:${it.image}").into(imWeather)

        })
    }


    private fun requestWeatherData(city: String) {
        val url = "https://api.weatherapi.com/v1/forecast.json?key=" +
                API_KEY +
                "&q=" +
                city +
                "&days=" +
                "3" +
                "&aqi=no&alerts=no"

        val queue = Volley.newRequestQueue(context) // Инстанция очереди
        val request = StringRequest( // Формирование запроса
            Request.Method.GET,
            url,
            { result ->
                parseWeatherData(result)

            },
            { error ->
                Log.d("LogInfo", "Error: $error")
            }
        )
        queue.add(request) // Добавляем запрос
    }


    private fun parseWeatherData(result: String) {
        val mainObject = JSONObject(result)
        val list = parseForecastLive(mainObject)
        parseData(mainObject,list[0])

    }

    private fun parseData(mainObject: JSONObject,weatherDayItem: WeatherDayItem) {
        val item = WeatherDayItem(
            city = mainObject.getJSONObject("location").getString("name"),
            date = mainObject.getJSONObject("current").getString("last_updated"),
            condition = mainObject.getJSONObject("current").getJSONObject("condition")
                .getString("text"),
            image = mainObject.getJSONObject("current").getJSONObject("condition")
                .getString("icon"),
            currentTemp = mainObject.getJSONObject("current").getString("temp_c"),
            minTemp = weatherDayItem.minTemp,
            maxTemp = weatherDayItem.maxTemp,
            hours = weatherDayItem.hours
        )

        mainViewModel.liveDataCurrent.value = item // Передаем значение


    }

    private fun parseForecastLive(mainObject: JSONObject): List<WeatherDayItem> {
        val list = ArrayList<WeatherDayItem>()
        val daysArray = mainObject.getJSONObject("forecast").getJSONArray("forecastday")
        for (i in 0 until daysArray.length()) { // беребираем весь массив
            val name = mainObject.getJSONObject("location").getString("name")
            val day = daysArray[i] as JSONObject
            val item = WeatherDayItem(
                city = name,
                date = day.getString("date"),
                condition = day.getJSONObject("day").getJSONObject("condition").getString("text"),
                minTemp = day.getJSONObject("day").getString("mintemp_c").toFloat().toInt().toString(),
                maxTemp =  day.getJSONObject("day").getString("maxtemp_c").toFloat().toInt().toString(),
                hours = day.getJSONArray("hour").toString(),
                image = "",
                currentTemp = ""
            )
            list.add(item)

        }
        mainViewModel.liveDataList.value = list
        return list

    }


    private fun permissionListener() {
        activityLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        }
    }


    private fun checkpermission() {
        if (!isPermissionsGranted(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionListener()
            activityLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance() = MainFragment()

    }
}