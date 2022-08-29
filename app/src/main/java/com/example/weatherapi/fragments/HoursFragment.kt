package com.example.weatherapi.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapi.R
import com.example.weatherapi.adapter.WeatherAdapter
import com.example.weatherapi.data.WeatherDayItem
import com.example.weatherapi.databinding.FragmentHoursBinding
import com.example.weatherapi.fragments.viewmodel.MainViewModel
import org.json.JSONArray
import org.json.JSONObject


class HoursFragment : Fragment() {
    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val mainViewModel: MainViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        mainViewModel.liveDataCurrent.observe(viewLifecycleOwner,{
               adapter.submitList(getHoursList(it))
        })

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun initRcView() =with(binding){
        adapter = WeatherAdapter()
        rcView.layoutManager = LinearLayoutManager(requireContext(),LinearLayoutManager.VERTICAL,false)
        rcView.adapter = adapter



    }

    private fun getHoursList(item : WeatherDayItem) : List<WeatherDayItem>{
        val hoursArray =  JSONArray(item.hours)
        val list = ArrayList<WeatherDayItem>()

        for(i in 0 until hoursArray.length()){

            val wItem = WeatherDayItem(
                city = item.city,
                date =  (hoursArray[i] as JSONObject).getString("time"),
                condition = (hoursArray[i] as JSONObject).getJSONObject("condition").getString("text"),
                currentTemp = (hoursArray[i] as JSONObject).getString("temp_c").toFloat().toInt().toString(),
                minTemp = "",
                maxTemp = "",
                image = (hoursArray[i] as JSONObject).getJSONObject("condition").getString("icon"),
                hours = ""
            )

            list.add(wItem)

        }

        return list


    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()

    }
}