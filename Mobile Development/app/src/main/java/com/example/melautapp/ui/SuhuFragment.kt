package com.example.melautapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.melautapp.R
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.databinding.FragmentSuhuBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.ViewModelProvider
class SuhuFragment : Fragment() {

    private var _binding: FragmentSuhuBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationViewModel: LocationViewModel

    // In SuhuFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuhuBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

        // Observe location data from ViewModel
        locationViewModel.locationData.observe(viewLifecycleOwner, { locationData ->
            updateUiWithLocationData(locationData)
        })

        return binding.root
    }

    private fun updateUiWithLocationData(locationData: LocationResponse?) {
        if (locationData != null) {
            binding.tvLocation.text = locationData.location
            binding.tvDate.text = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(Date())
            binding.tvTemperature.text = "${locationData.temperature}°C"
            binding.tvWeatherStatus.text = locationData.weather
            // Set weather icon dynamically based on weather status
            val weatherIconRes = R.drawable.ic_weather_cloudy // Example: Replace with actual logic
            binding.ivWeatherIcon.setImageResource(weatherIconRes)
        } else {
            // Fallback if no location data is available
            binding.tvLocation.text = "Lokasi tidak tersedia"
            binding.tvTemperature.text = "--°C"
            binding.tvWeatherStatus.text = "Tidak diketahui"
            binding.ivWeatherIcon.setImageResource(R.drawable.ic_weather_cloudy)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
