package com.MobileComputing.BeerTracker

import android.app.Activity
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.util.*
import org.jetbrains.anko.doAsync
import java.lang.Exception
import kotlin.collections.ArrayList

class MapView : Fragment(), OnMapReadyCallback {

    private lateinit var googleMap : GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var latLong: LatLng
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(
                    R.layout.view_map, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment

        mapFragment.getMapAsync(this)

    }

    override fun onMapReady(map: GoogleMap?) {

        googleMap = map!!
        if(context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_FINE_LOCATION) } == PackageManager.PERMISSION_GRANTED
            || context?.let { ContextCompat.checkSelfPermission(it, android.Manifest.permission.ACCESS_COARSE_LOCATION) } == PackageManager.PERMISSION_GRANTED)
        {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
            fusedLocationClient.lastLocation.addOnSuccessListener { location : Location? ->
                if(location!=null)
                {
                    latLong = LatLng(location.latitude, location.longitude)
                    with ( googleMap)
                    {
                        animateCamera(CameraUpdateFactory.newLatLngZoom(latLong, 13f))
                    }
                }
            }
        }
        else
        {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION), 123)
        }
    }

    companion object {
        fun newInstance(): MapView = MapView()
    }
}

