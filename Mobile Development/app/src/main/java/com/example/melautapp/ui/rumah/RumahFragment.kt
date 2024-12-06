package com.example.melautapp.ui.rumah

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.example.melautapp.data.response.LocationResponse
import com.example.melautapp.data.retrofit.ApiConfig
import com.example.melautapp.databinding.FragmentRumahBinding
import com.google.android.gms.location.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

// Fragment untuk menangani pengambilan dan penampilan lokasi pengguna
class RumahFragment : Fragment() {

    // Variabel untuk binding ke layout fragment
    private var _binding: FragmentRumahBinding? = null
    private val binding get() = _binding!!

    // Menyimpan referensi untuk FusedLocationProviderClient yang digunakan untuk mengambil lokasi
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val TAG = "RumahFragment"

    // Menyimpan status apakah lokasi sudah berhasil diambil
    private var isLocationFetched = false
    private lateinit var locationCallback: LocationCallback

    // Memanggil onCreateView untuk menginisialisasi tampilan
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inisialisasi binding untuk mengakses elemen-elemen UI
        _binding = FragmentRumahBinding.inflate(inflater, container, false)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())

        initUI() // Menyiapkan UI
        checkPermissionsAndFetchLocation() // Mengecek izin dan mengambil lokasi

        return binding.root
    }

    // Menyiapkan UI awal
    private fun initUI() {
        binding.progressBar.visibility = View.GONE // Menyembunyikan progress bar
        binding.textRumah.text = "Mengambil lokasi..." // Menampilkan pesan awal
    }

    // Mengecek izin lokasi dan GPS, jika tersedia maka ambil lokasi
    private fun checkPermissionsAndFetchLocation() {
        if (hasLocationPermission()) {
            if (isGpsEnabled()) { // Jika GPS aktif, ambil lokasi
                if (!isLocationFetched) {
                    fetchCurrentLocation()
                }
            } else {
                binding.textRumah.text = "Harap aktifkan GPS untuk melanjutkan." // Menampilkan pesan jika GPS tidak aktif
            }
        } else {
            requestLocationPermission() // Meminta izin jika belum diberikan
        }
    }

    // Mengecek apakah aplikasi sudah mendapatkan izin lokasi
    private fun hasLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Meminta izin lokasi kepada pengguna
    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            100 // Kode permintaan izin
        )
    }

    // Callback untuk hasil permintaan izin
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkPermissionsAndFetchLocation() // Jika izin diterima, coba ambil lokasi
        } else {
            binding.textRumah.text = "Akses lokasi ditolak." // Pesan jika izin ditolak
        }
    }

    // Mengecek apakah GPS aktif
    private fun isGpsEnabled(): Boolean {
        val locationManager = requireContext().getSystemService(LocationManager::class.java)
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    // Mengambil lokasi pengguna
    @SuppressLint("MissingPermission") // Menandakan bahwa izin lokasi sudah dicek sebelumnya
    private fun fetchCurrentLocation() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Interval pengambilan lokasi
            fastestInterval = 5000 // Interval tercepat
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Prioritas akurasi tinggi
        }

        binding.progressBar.visibility = View.VISIBLE // Menampilkan progress bar saat menunggu lokasi

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                p0?.let { result ->
                    if (result.locations.isNotEmpty()) {
                        val location = result.locations[0]
                        val lat = location.latitude
                        val lon = location.longitude
                        fetchLocationData(lat.toString(), lon.toString()) // Ambil data lokasi dari API
                        fusedLocationProviderClient.removeLocationUpdates(locationCallback) // Hentikan pembaruan lokasi
                        isLocationFetched = true // Menandakan lokasi sudah berhasil diambil
                        binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                    }
                }
            }
        }

        // Meminta pembaruan lokasi secara terus-menerus
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // Mengambil data lokasi dari server
    private fun fetchLocationData(lat: String, lon: String) {
        binding.progressBar.visibility = View.VISIBLE // Menampilkan progress bar
        val apiService = ApiConfig.getApiService()
        apiService.getLocations().enqueue(object : Callback<List<LocationResponse>> {
            override fun onResponse(
                call: Call<List<LocationResponse>>,
                response: Response<List<LocationResponse>>
            ) {
                binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                if (response.isSuccessful) {
                    processLocationData(lat.toDouble(), lon.toDouble(), response.body()) // Proses data lokasi
                } else {
                    binding.textRumah.text = "Gagal mengambil data lokasi." // Pesan gagal jika API tidak berhasil
                }
            }

            override fun onFailure(call: Call<List<LocationResponse>>, t: Throwable) {
                binding.progressBar.visibility = View.GONE // Sembunyikan progress bar
                binding.textRumah.text = "Kesalahan jaringan: ${t.localizedMessage}" // Pesan jika ada kesalahan jaringan
            }
        })
    }

    // Memproses data lokasi yang diterima dari API
    private fun processLocationData(lat: Double, lon: Double, locations: List<LocationResponse>?) {
        if (locations.isNullOrEmpty()) {
            binding.textRumah.text = "Tidak ada data lokasi yang tersedia." // Pesan jika tidak ada data lokasi
            return
        }

        // Mencari lokasi terdekat dengan menghitung selisih latitude dan longitude
        val closestLocation = locations.minByOrNull { location ->
            val latDiff = Math.abs(lat - location.lat.toDouble())
            val lonDiff = Math.abs(lon - location.lon.toDouble())
            latDiff + lonDiff
        }

        // Menampilkan data lokasi terdekat
        closestLocation?.let {
            binding.textRumah.text = """
                ID: ${it.id}
                Provinsi: ${it.propinsi}
                Kota: ${it.kota}
                Kecamatan: ${it.kecamatan}
                Latitude: ${it.lat}
                Longitude: ${it.lon}
            """.trimIndent()
        } ?: run {
            binding.textRumah.text = "Tidak ditemukan lokasi terdekat." // Pesan jika tidak ada lokasi terdekat
        }
    }

    // Menghentikan pembaruan lokasi saat tampilan dihancurkan
    override fun onDestroyView() {
        super.onDestroyView()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback) // Menghapus pembaruan lokasi
        _binding = null // Melepas referensi binding
    }
}
