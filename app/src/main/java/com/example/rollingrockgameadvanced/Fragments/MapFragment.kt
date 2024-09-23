package com.example.rollingrockgameadvanced.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.rollingrockgameadvanced.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapFragment : Fragment() {
    private var map: GoogleMap? = null

    private val callback = OnMapReadyCallback { googleMap ->
        val sydney = LatLng(-33.86947276307224, 151.2101295790259)
        map = googleMap
        map?.apply {
            addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
            moveCamera(CameraUpdateFactory.newLatLng(sydney))
        }
    }

    fun changeLocation(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        map?.apply {
            addMarker(MarkerOptions().position(latLng).title("Player position"))
            animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17.0f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(callback)
    }
}