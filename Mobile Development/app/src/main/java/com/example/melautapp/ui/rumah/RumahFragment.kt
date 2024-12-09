package com.example.melautapp.ui.rumah

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.databinding.FragmentRumahBinding
import com.example.melautapp.ui.SuhuFragment
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RumahFragment : Fragment() {

    private var _binding: FragmentRumahBinding? = null
    private val binding get() = _binding!!

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var isLocationFetched = false
    private lateinit var locationCallback: LocationCallback

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRumahBinding.inflate(inflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initUI()
        checkPermissionsAndFetchLocation()

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
                binding.textRumah.text = "Akses lokasi ditolak."
            }
        }
    }

    private fun isGpsEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    @SuppressLint("MissingPermission")
    private fun fetchCurrentLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000
            fastestInterval = 5000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        binding.progressBar.visibility = View.VISIBLE

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0?.let { result ->
                    if (result.locations.isNotEmpty()) {
                        val location = result.locations[0]
                        val lat = location.latitude
                        val lon = location.longitude
                        fetchLocationData(lat.toString(), lon.toString())
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
                        isLocationFetched = true
                        binding.progressBar.visibility = View.GONE
                    }
                }
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }

    private fun fetchLocationData(lat: String, lon: String) {
        binding.progressBar.visibility = View.VISIBLE
        val apiService = ApiConfig.getApiService()
        apiService.getLocations().enqueue(object : Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                binding.progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    processLocationData(lat.toDouble(), lon.toDouble(), response.body())
                } else {
                    binding.textRumah.text = "Gagal mengambil data lokasi."
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                binding.textRumah.text = "Kesalahan jaringan: ${t.localizedMessage}"
            }
        })
    }

    private fun processLocationData(
        lat: Double,
        lon: Double,
        locations: List<LocationResponse>?
    ) {
        if (locations.isNullOrEmpty()) {
            binding.textRumah.text = "Tidak ada data lokasi yang tersedia."
            return
        }

        val closestLocation = locations.minByOrNull { location ->
            val latDiff = Math.abs(lat - (location.lat?.toDouble() ?: 0.0))
            val lonDiff = Math.abs(lon - (location.lon?.toDouble() ?: 0.0))
            latDiff + lonDiff
        }

        requireActivity().runOnUiThread {
            closestLocation?.let {
                binding.textRumah.text = """
                ID: ${it.id}
                Provinsi: ${it.propinsi}
                Kota: ${it.kota}
                Kecamatan: ${it.kecamatan}
                Latitude: ${it.lat}
                Longitude: ${it.lon}
            """.trimIndent()

                // Create an instance of the SuhuFragment and pass location data
                val suhuFragment = SuhuFragment.newInstance(it)

                // Add SuhuFragment to the container directly (without replacing)
                val fragmentContainer = binding.suhuFragmentContainer
                if (fragmentContainer != null) {
                    parentFragmentManager.beginTransaction()
                        .add(fragmentContainer.id, suhuFragment)
                        .commit()
                }
            } ?: run {
                binding.textRumah.text = "Tidak ditemukan lokasi terdekat."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
