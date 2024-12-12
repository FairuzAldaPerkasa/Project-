package com.example.melautapp.ui.rumah

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.databinding.FragmentRumahBinding
import com.example.melautapp.ui.DetailPrediksi
import com.example.melautapp.ui.DetailSuhuFragment
import com.example.melautapp.ui.MainViewModel
import com.example.melautapp.ui.MapsFragment
import com.example.melautapp.ui.SuhuFragment
import com.google.android.gms.location.*
import kotlinx.coroutines.launch
import retrofit2.Response
class RumahFragment : Fragment() {

    private var _binding: FragmentRumahBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isLocationFetched = false
    private lateinit var locationCallback: LocationCallback
    private lateinit var mainViewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRumahBinding.inflate(inflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        initUI()
        checkPermissionsAndFetchLocation()

        // Add fragments to their containers
        loadFragments()

        return binding.root
    }

    private fun initUI() {
        binding.progressBar.visibility = View.GONE
        binding.textRumah.text = "Mengambil lokasi..."
    }

    private fun checkPermissionsAndFetchLocation() {
        if (hasLocationPermission()) {
            if (isGpsEnabled()) {
                if (!isLocationFetched) {
                    fetchCurrentLocation()
                }
            } else {
                binding.textRumah.text = "Harap aktifkan GPS untuk melanjutkan."
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionsAndFetchLocation()
            } else {
                binding.textRumah.text = "Akses lokasi ditolak. Harap izinkan akses lokasi untuk melanjutkan."
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    val lat = location.latitude
                    val lon = location.longitude
                    displayLocation(lat, lon)
                } else {
                    getLastKnownLocation()
                }
            } else {
                Log.e("LocationError", "Error fetching last location: ${task.exception?.localizedMessage}")
                binding.textRumah.text = "Gagal mengambil lokasi."
                binding.progressBar.visibility = View.GONE
            }
        }
    }


    private fun getLastKnownLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 5000
            fastestInterval = 2000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.locations.firstOrNull()?.let { location ->
                    val lat = location.latitude
                    val lon = location.longitude
                    displayLocation(lat, lon)
                    fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                }
            }
        }

        try {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        } catch (e: SecurityException) {
            Log.e("LocationError", "Permission issue: ${e.localizedMessage}")
            binding.textRumah.text = "Permission denied."
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun displayLocation(lat: Double, lon: Double) {
        Log.d("LocationInfo", "Latitude: $lat, Longitude: $lon")
        binding.progressBar.visibility = View.GONE
        loadFragments()
        fetchWeatherData(lat, lon)

        // Update ViewModel with coordinates
        mainViewModel.setCoordinates(lat, lon)
    }

    private fun fetchWeatherData(lat: Double, lon: Double) {
        val apiService = ApiConfig.getApiService()

        // Launch coroutine to fetch weather data
        lifecycleScope.launch {
            try {
                // Make the API call (using suspend function)
                val response = apiService.getLocation(lat, lon)

                // Check if the response is successful
                if (response.isSuccessful) {
                    val locationResponse = response.body()
                    if (locationResponse != null) {
                        Log.d("WeatherResponse", "Location: ${locationResponse.location}")
                        Log.d("WeatherResponse", "Temperature: ${locationResponse.temperature}°C")
                        Log.d("WeatherResponse", "Weather: ${locationResponse.weather}")

                        // Update the ViewModel with location and weather data
                        mainViewModel.setLocationResponse(locationResponse)

                    } else {
                        Log.e("WeatherError", "Location response body is null.")
                        binding.textRumah.text = "Tidak ada data cuaca yang ditemukan."
                    }
                } else {
                    Log.e("WeatherError", "Failed to fetch weather data. Code: ${response.code()} - ${response.message()}")
                    binding.textRumah.text = "Gagal mendapatkan data cuaca."
                }
            } catch (e: Exception) {
                Log.e("WeatherError", "Error fetching weather data: ${e.localizedMessage}")
                binding.textRumah.text = "Terjadi kesalahan saat mengambil data cuaca."
            }
        }
    }

    private fun loadFragments() {
        // Only replace fragments if not already present
        if (childFragmentManager.findFragmentByTag(SuhuFragment::class.java.simpleName) == null) {
            val suhuFragment = SuhuFragment()
            childFragmentManager.beginTransaction()
                .replace(binding.suhuFragmentContainer.id, suhuFragment, SuhuFragment::class.java.simpleName)
                .commit()
        }

        if (childFragmentManager.findFragmentByTag(MapsFragment::class.java.simpleName) == null) {
            val mapsFragment = MapsFragment()
            childFragmentManager.beginTransaction()
                .replace(binding.mapsFragmentContainer.id, mapsFragment, MapsFragment::class.java.simpleName)
                .commit()
        }

        if (childFragmentManager.findFragmentByTag(DetailSuhuFragment::class.java.simpleName) == null) {
            val detailSuhuFragment = DetailSuhuFragment()
            childFragmentManager.beginTransaction()
                .replace(binding.detailSuhuFragmentContainer.id, detailSuhuFragment, DetailSuhuFragment::class.java.simpleName)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
