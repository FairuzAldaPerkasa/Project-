package com.example.melautapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.melautapp.R
import com.example.melautapp.databinding.FragmentMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!

    private var googleMap: GoogleMap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        // Inisialisasi tombol
        binding.btnPantauLaut.setOnClickListener {
            // Navigasi ke DetailPrediksi
            val intent = Intent(requireContext(), DetailPrediksi::class.java)
            startActivity(intent)
        }

        // Initialize the SupportMapFragment and set the callback
//        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
//        mapFragment?.getMapAsync(this)

        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        // Tambahkan marker contoh
        val sampleLocation = LatLng(-8.65, 115.2167) // Koordinat Denpasar
        googleMap?.addMarker(MarkerOptions().position(sampleLocation).title("Denpasar"))

        // Pindahkan kamera ke lokasi
        googleMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(sampleLocation, 12f))
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(): MapsFragment {
            return MapsFragment()
        }
    }
}
