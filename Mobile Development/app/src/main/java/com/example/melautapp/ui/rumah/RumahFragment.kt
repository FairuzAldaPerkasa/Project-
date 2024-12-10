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
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.databinding.FragmentRumahBinding
import com.example.melautapp.ui.DetailPrediksi
import com.example.melautapp.ui.DetailSuhuFragment
import com.example.melautapp.ui.LocationViewModel
import com.example.melautapp.ui.MapsFragment
import com.example.melautapp.ui.SuhuFragment
import com.google.android.gms.location.*
import retrofit2.Call

class RumahFragment : Fragment() {

    private var _binding: FragmentRumahBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isLocationFetched = false
    private lateinit var locationCallback: LocationCallback
    private lateinit var locationViewModel: LocationViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRumahBinding.inflate(inflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationViewModel = ViewModelProvider(requireActivity()).get(LocationViewModel::class.java)

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

        // Periksa apakah ini adalah permintaan izin lokasi
        if (requestCode == 100) {
            // Jika izin diberikan, lanjutkan untuk memeriksa dan mendapatkan lokasi
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionsAndFetchLocation()
            } else {
                // Jika izin tidak diberikan, beri tahu pengguna
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
            // Jika izin tidak diberikan, tidak lanjutkan
            return
        }

        // Coba mengambil lokasi terakhir yang diketahui terlebih dahulu
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val location = task.result
                if (location != null) {
                    // Lokasi ditemukan dengan cepat
                    val lat = location.latitude
                    val lon = location.longitude
                    displayLocation(lat, lon)
                } else {
                    // Jika lokasi terakhir tidak ditemukan, minta pembaruan lokasi segera
                    Log.w("LocationError", "Last location is null, requesting updates...")
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
            interval = 5000  // Interval update lokasi lebih cepat
            fastestInterval = 2000  // Interval tercepat
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
        // Display location in the TextView
        Log.d("LocationInfo", "Latitude: $lat, Longitude: $lon")

        // Make sure the progress bar is hidden
        binding.progressBar.visibility = View.GONE

        // Dynamically load the fragments once the location is available
        loadFragments()

        // Fetch weather data using Retrofit with the obtained lat and lon
        fetchWeatherData(lat, lon)

        val intent = Intent(requireContext(), DetailPrediksi::class.java)
        intent.putExtra("LATITUDE", lat)
        intent.putExtra("LONGITUDE", lon)
        startActivity(intent)
    }


    private fun fetchWeatherData(lat: Double, lon: Double) {
        // Get the API service instance
        val apiService = ApiConfig.getApiService()

        // Make the API call to get location data based on latitude and longitude
        apiService.getLocation(lat, lon).enqueue(object : retrofit2.Callback<LocationResponse> {

            // Callback when the response is received
            override fun onResponse(
                call: Call<LocationResponse>,
                response: retrofit2.Response<LocationResponse>
            ) {
                Log.d("WeatherResponse", "Response code: ${response.code()}")
                if (response.isSuccessful) {
                    val locationResponse = response.body()
                    if (locationResponse != null) {
                        // Log the response body details for debugging
                        Log.d("WeatherResponse", "Location: ${locationResponse.location}")
                        Log.d("WeatherResponse", "Temperature: ${locationResponse.temperature}°C")
                        Log.d("WeatherResponse", "Weather: ${locationResponse.weather}")

                        // Update the ViewModel with location and weather data
                        locationViewModel.setLocationData(locationResponse)

                        // Optionally log success or any further actions you need
                        Log.d("WeatherResponse", "Weather data updated successfully.")

                        // No UI update here (if you're choosing to only log data instead of displaying)
                        // This can be used to handle the logic as per your requirements

                        // Uncomment below to show data in the UI:
                        // binding.textRumah.text = """
                        //     Location: ${locationResponse.location}
                        //     Temperature: ${locationResponse.temperature}°C
                        //     Weather: ${locationResponse.weather}
                        // """.trimIndent()

                    } else {
                        // Handle the case where the response body is null
                        Log.e("WeatherError", "Location response body is null.")
                        binding.textRumah.text = "Tidak ada data cuaca yang ditemukan."
                    }
                } else {
                    // Log the error when the response code is not successful
                    Log.e("WeatherError", "Failed to fetch weather data. Code: ${response.code()} - ${response.message()}")
                    binding.textRumah.text = "Gagal mendapatkan data cuaca."
                }
            }

            // Callback for failure in making the API call
            override fun onFailure(call: Call<LocationResponse>, t: Throwable) {
                // Log the error details
                Log.e("WeatherError", "Error fetching weather data: ${t.localizedMessage}")

                // Set an error message to display on UI
                binding.textRumah.text = "Terjadi kesalahan saat mengambil data cuaca."
            }
        })
    }




    private fun loadFragments() {
        // Add SuhuFragment to the layout dynamically below the TextView
        val suhuFragment = SuhuFragment()
        childFragmentManager.beginTransaction()
            .add(binding.suhuFragmentContainer.id, suhuFragment)
            .commit()


        // Add DetailSuhuFragment to the layout below MapsFragment
        val mapsFragment = MapsFragment()
        childFragmentManager.beginTransaction()
            .add(binding.mapsFragmentContainer.id, mapsFragment)
            .commit()

        val detailSuhuFragment = DetailSuhuFragment()
        childFragmentManager.beginTransaction()
            .add(binding.detailSuhuFragmentContainer.id, detailSuhuFragment)
            .commit()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
