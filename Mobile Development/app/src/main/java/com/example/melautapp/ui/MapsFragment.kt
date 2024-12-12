package com.example.melautapp.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.melautapp.R
import com.example.melautapp.databinding.FragmentMapsBinding
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapsFragment : Fragment() {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var map: MapView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)

        // Initialize the ViewModel
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)

        // Configure OSMDroid settings
        Configuration.getInstance().load(requireContext(), requireActivity().getPreferences(0))

        // Set up the map
        map = binding.mapView
        map.setTileSource(TileSourceFactory.MAPNIK)

        // Set up marker for dynamic location
        mainViewModel.latitude.observe(viewLifecycleOwner) { lat ->
            mainViewModel.longitude.observe(viewLifecycleOwner) { lon ->
                if (lat != null && lon != null) {
                    val location = GeoPoint(lat, lon)
                    val marker = Marker(map)
                    marker.position = location
                    marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                    map.overlays.clear()
                    map.overlays.add(marker)

                    val mapController = map.controller
                    mapController.setZoom(15.0)
                    mapController.setCenter(location)
                }
            }
        }

        // Set up the button click listener to navigate to DetailPrediksi with coordinates
        binding.btnPantauLaut.setOnClickListener {
            val lat = mainViewModel.latitude.value
            val lon = mainViewModel.longitude.value

            if (lat != null && lon != null) {
                val intent = Intent(requireContext(), DetailPrediksi::class.java)
                intent.putExtra("LATITUDE", lat)
                intent.putExtra("LONGITUDE", lon)
                startActivity(intent)
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
