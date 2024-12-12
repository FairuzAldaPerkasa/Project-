package com.example.melautapp.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.melautapp.R
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

    @SuppressLint("ResourceAsColor")
    private fun updateUiWithPredictionResult(result: ResultResponse?, lat: Double, lon: Double) {
        if (result != null) {
            val inputData = result.inputData

            // Display input data and predicted results
            binding.detailPrediksiLayout.setBackgroundColor(resources.getColor(R.color.blue_500, theme))
            binding.textViewTn.text = "Tn (Min Temp): ${result.inputData.tn} \u00B0C"
            binding.textViewTx.text = "Tx (Max Temp): ${result.inputData.tx} \u00B0C"
            binding.textViewTavg.text = "Tavg (Avg Temp): ${result.inputData.tavg} \u00B0C"
            binding.textViewRHAvg.text = "RH_avg (Avg Humidity): ${result.inputData.rHAvg}%"
            binding.textViewFfAvg.text = "ff_avg (Avg Wind Speed): ${result.inputData.ffAvg} m/s"
            binding.textViewPredictedCondition.text = "Predicted Condition: ${result.predictedCondition}"
            binding.textViewPredictedRad.text = "Predicted Radar: ${result.predictedRad}"

            // Show warning as a dialog if condition is unsafe
            if (result.predictedCondition.lowercase() != "aman") {
                showWarningDialog(result.predictedCondition)
                binding.textViewTitle.setTextColor(resources.getColor(R.color.white,theme))
                binding.detailPrediksiLayout.setBackgroundColor(resources.getColor(R.color.red, theme))
                binding.buttonBack.setBackgroundColor(R.color.white)
            }
        } else {
            // Display message if no result is available
            binding.textViewTn.text = "Tn (Min Temp): --"
            binding.textViewTx.text = "Tx (Max Temp): --"
            binding.textViewTavg.text = "Tavg (Avg Temp): --"
            binding.textViewRHAvg.text = "RH_avg (Avg Humidity): --"
            binding.textViewFfAvg.text = "ff_avg (Avg Wind Speed): --"
            binding.textViewPredictedCondition.text = "Predicted Condition: --"
            binding.textViewPredictedRad.text = "Predicted Radiation: --"
        }
    }

    private fun showWarningDialog(condition: String) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Warning")
        builder.setMessage("Unsafe condition detected: $condition. Please proceed with caution!")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        builder.show()
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}