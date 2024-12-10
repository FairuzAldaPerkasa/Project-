package com.example.melautapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.melautapp.data.response.LocationResponse

class LocationViewModel : ViewModel() {
    private val _locationData = MutableLiveData<LocationResponse>()
    val locationData: LiveData<LocationResponse> get() = _locationData

    // Function to update location data
    fun setLocationData(location: LocationResponse) {
        _locationData.value = location
    }
}
