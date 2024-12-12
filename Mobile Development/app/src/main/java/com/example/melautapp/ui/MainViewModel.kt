package com.example.melautapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.response.ResultResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.data.retrofit.ResultRequest
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _resultResponse = MutableLiveData<ResultResponse?>()
    private val _locationResponse = MutableLiveData<LocationResponse?>()
    private val _latitude = MutableLiveData<Double>()
    private val _longitude = MutableLiveData<Double>()
    private val apiService = ApiConfig.getApiService()


    val resultResponse: LiveData<ResultResponse?> get() = _resultResponse
    val locationResponse: LiveData<LocationResponse?> get() = _locationResponse
    val latitude: LiveData<Double> get() = _latitude
    val longitude: LiveData<Double> get() = _longitude

    fun setLocationResponse(location: LocationResponse) {
        _locationResponse.value = location
    }

    fun setCoordinates(lat: Double, lon: Double) {
        _latitude.value = lat
        _longitude.value = lon
    }

    fun fetchLocationData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                val response = ApiConfig.getApiService().getLocation(lat, lon)
                if (response.isSuccessful) {
                    _locationResponse.postValue(response.body())
                } else {
                    _locationResponse.postValue(null)
                    Log.e("APIError", "Error fetching location data: ${response.code()} - ${response.message()}")
                }
            } catch (e: Exception) {
                _locationResponse.postValue(null)
                Log.e("APIError", "Error fetching location data: ${e.localizedMessage}")
            }
        }
    }

    // Fetch result data with logging
    fun fetchResultData(lat: Double, lon: Double) {
        viewModelScope.launch {
            try {
                // Buat objek ResultRequest dengan lat dan lon
                val resultRequest = ResultRequest(lat, lon)

                // Panggil API dengan resultRequest
                val response = apiService.postResult(resultRequest)

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        _resultResponse.postValue(body)  // Update LiveData dengan hasil dari API
                    } else {
                        Log.e("MainViewModel", "Response body is null")
                        _resultResponse.postValue(null)  // Tangani body null jika diperlukan
                    }
                } else {
                    Log.e("MainViewModel", "API error: ${response.code()} - ${response.message()}")
                    _resultResponse.postValue(null)  // Tangani error jika respons API tidak berhasil
                }
            } catch (e: Exception) {
                Log.e("MainViewModel", "Error: ${e.message}")
                _resultResponse.postValue(null)  // Tangani exception jika terjadi error
            }
        }
    }

}
