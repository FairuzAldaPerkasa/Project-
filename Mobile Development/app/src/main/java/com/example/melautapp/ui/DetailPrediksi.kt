package com.example.melautapp.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.melautapp.data.response.ResultResponse
import com.example.melautapp.databinding.DetailPrediksiBinding

class DetailPrediksi : AppCompatActivity() {

    private var _binding: DetailPrediksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DetailPrediksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewModel
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        // Retrieve latitude and longitude from Intent
        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lon = intent.getDoubleExtra("LONGITUDE", 0.0)

        // Set coordinates in ViewModel
        mainViewModel.setCoordinates(lat, lon)

        // Observe resultResponse LiveData from ViewModel
        mainViewModel.resultResponse.observe(this, Observer { result ->
            updateUiWithPredictionResult(result, lat, lon)
        })

        // Fetch prediction result if not loaded yet
        if (mainViewModel.resultResponse.value == null) {
            Log.d("DetailPrediksi", "Fetching result data for coordinates: lat=$lat, lon=$lon")
            mainViewModel.fetchResultData(lat, lon)
        }

        // Set up Back Button
        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun updateUiWithPredictionResult(result: ResultResponse?, lat: Double, lon: Double) {
        if (result != null) {
            // Tampilkan hasil prediksi dengan benar
            binding.textViewStatus.text = "Condition: ${result.predictedCondition} \nLatitude: $lat \nLongitude: $lon"
            binding.textViewResult.text = "Predicted Radiation: ${result.predictedRad} \nLatitude: $lat \nLongitude: $lon"
        } else {
            // Jika tidak ada hasil, tampilkan pesan
            binding.textViewResult.text = "No result available \nLatitude: $lat \nLongitude: $lon"
            binding.textViewStatus.text = "Unable to fetch prediction data \nLatitude: $lat \nLongitude: $lon"
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
