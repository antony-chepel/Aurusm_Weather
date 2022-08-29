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
import com.example.weatherapi.databinding.FragmentDaysBinding
import com.example.weatherapi.fragments.viewmodel.MainViewModel

class DaysFragment : Fragment() {

    private lateinit var binding: FragmentDaysBinding

    private lateinit var adapter: WeatherAdapter

    private val model : MainViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDaysBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        model.liveDataList.observe(viewLifecycleOwner,{
            adapter.submitList(it.subList(1,it.size))
        })
    }

    private fun init() = with(binding){
        adapter = WeatherAdapter()

        rcView.adapter = adapter

        rcView.layoutManager = LinearLayoutManager(activity)
    }

    companion object {
        @JvmStatic
        fun newInstance() = DaysFragment()
    }
}