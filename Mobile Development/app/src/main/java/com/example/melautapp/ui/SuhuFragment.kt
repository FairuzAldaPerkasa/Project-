package com.example.melautapp.ui

import android.annotation.SuppressLint
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
import com.bumptech.glide.Glide
import com.example.melautapp.ui.MainViewModel

class SuhuFragment : Fragment() {

    private var _binding: FragmentSuhuBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    // In SuhuFragment

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSuhuBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Observe location data from ViewModel
        mainViewModel.locationResponse.observe(viewLifecycleOwner, { locationData ->
            updateUiWithLocationData(locationData)
        })

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun updateUiWithLocationData(locationData: LocationResponse?) {
        if (locationData != null) {
            binding.tvLocation.text = locationData.location
            binding.tvDate.text = SimpleDateFormat("EEE, MMM dd", Locale.getDefault()).format(Date())
            binding.tvTemperature.text = "${locationData.temperature}°C"
            binding.tvWeatherStatus.text = locationData.weather
            // Set weather icon dynamically based on weather status
            binding.tvWeatherStatus.text = locationData.weather
            // Set weather icon dynamically based on weather status
            Glide.with(binding.ivWeatherIcon.context)
                .load(locationData.iconUrl) // URL gambar
                .placeholder(R.drawable.placeholder_icon) // Ikon placeholder saat memuat
                .error(R.drawable.error_icon) // Ikon jika gagal memuat
                .into(binding.ivWeatherIcon)
        } else {
            // Fallback if no location data is available
            binding.tvLocation.text = "Lokasi tidak tersedia"
            binding.tvTemperature.text = "--°C"
            binding.tvWeatherStatus.text = "Tidak diketahui"
            binding.ivWeatherIcon.setImageResource(R.drawable.default_icon)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
