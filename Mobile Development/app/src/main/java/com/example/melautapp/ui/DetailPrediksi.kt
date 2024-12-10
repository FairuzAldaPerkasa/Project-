package com.example.melautapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.melautapp.R
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.response.ResultResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.data.retrofit.ResultRequest
import com.example.melautapp.databinding.DetailPrediksiBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailPrediksi : AppCompatActivity() {

    private var _binding: DetailPrediksiBinding? = null
    private val binding get() = _binding!!

    private lateinit var locationViewModel: LocationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = DetailPrediksiBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve latitude and longitude from Intent
        val lat = intent.getDoubleExtra("LATITUDE", 0.0)
        val lon = intent.getDoubleExtra("LONGITUDE", 0.0)

        // Post the prediction result using lat and lon
        postPredictionResult(lat, lon)
    }

    // Post location data (lat, lon) and fetch prediction result
    private fun postPredictionResult(lat: Double, lon: Double) {
        val request = ResultRequest(lat, lon)

        val weatherService = ApiConfig.getApiService()

        // Post the request and fetch the ResultResponse
        weatherService.postResult(request).enqueue(object : Callback<ResultResponse> {
            override fun onResponse(call: Call<ResultResponse>, response: Response<ResultResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()
                    updateUiWithPredictionResult(result)
                } else {
                    binding.textViewResult.text = "Error fetching data: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<ResultResponse>, t: Throwable) {
                binding.textViewResult.text = "Network error: ${t.localizedMessage}"
            }
        })
    }

    // Update UI with the prediction result
    private fun updateUiWithPredictionResult(result: ResultResponse?) {
        if (result != null) {
            binding.textViewStatus.text = "Condition: ${result.predictedCondition}"
            binding.textViewResult.text = "Predicted Radiation: ${result.predictedRad ?: "N/A"}"
        } else {
            binding.textViewResult.text = "No result available"
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
