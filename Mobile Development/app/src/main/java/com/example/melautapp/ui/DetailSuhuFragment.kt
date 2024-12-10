package com.example.melautapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.melautapp.R
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.databinding.FragmentDetailSuhuBinding

@Suppress("DEPRECATION")
class DetailSuhuFragment : Fragment() {

    private var _binding: FragmentDetailSuhuBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationViewModel: LocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailSuhuBinding.inflate(inflater, container, false)

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
            binding.apply {
                textUvIndex.text = "${locationData.pressure}"
                textHumidity.text = "${locationData.humidity}"
                textWind.text = "${locationData.windSpeed}"
            }
        } else {
            // Fallback if no location data is available
            binding.textUvIndex.text = "Data tidak tersedia"
            binding.textHumidity.text = "Data tidak tersedia"
            binding.textWind.text = "Data tidak tersedia"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
