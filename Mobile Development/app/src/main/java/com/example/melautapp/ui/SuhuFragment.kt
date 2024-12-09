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
class SuhuFragment : Fragment() {

    private var _binding: FragmentSuhuBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Retrieve location data from saved instance state if available
        if (savedInstanceState != null) {
            val locationData = savedInstanceState.getParcelable<LocationResponse>("location_data")
            updateUiWithLocationData(locationData)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuhuBinding.inflate(inflater, container, false)

        // Retrieve location data either from arguments or savedInstanceState
        val locationData = savedInstanceState?.getParcelable<LocationResponse>("location_data")
            ?: arguments?.getParcelable("location_data")
        updateUiWithLocationData(locationData)

        return binding.root
    }

    private fun updateUiWithLocationData(locationData: LocationResponse?) {
        if (locationData != null) {
            // Display location
            binding.tvLocation.text = "${locationData.kota}, ${locationData.propinsi}"
            // Display current date
            binding.tvDate.text = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(Date())

            // Example: Fetching temperature and weather data
            val temperature = "25°C"  // Ideally, get this data from a weather API or repository
            val weatherStatus = "Cerah" // Similarly, this should come from a weather API
            val weatherIconRes = R.drawable.ic_weather_cloudy // Weather icon resource

            // Update UI with dynamic data
            binding.tvTemperature.text = temperature
            binding.tvWeatherStatus.text = weatherStatus
            binding.ivWeatherIcon.setImageResource(weatherIconRes)
        } else {
            // If no location data is available, display a fallback message
            binding.tvLocation.text = "Lokasi tidak tersedia"
            binding.tvTemperature.text = "--°C"  // Indicating no temperature data
            binding.tvWeatherStatus.text = "Tidak diketahui"  // Indicating unknown weather status
            binding.ivWeatherIcon.setImageResource(R.drawable.ic_weather_cloudy)  // Fallback icon
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        val locationData = arguments?.getParcelable<LocationResponse>("location_data")
        outState.putParcelable("location_data", locationData)
    }

    companion object {
        fun newInstance(locationData: LocationResponse): SuhuFragment {
            val fragment = SuhuFragment()
            val args = Bundle()
            args.putParcelable("location_data", locationData)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
