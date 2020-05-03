package com.MobileComputing.BeerTracker

import android.app.Activity
import android.content.pm.PackageManager
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
    private var TAG = "MapView"
    private var beers : List<BeerItem> = listOf()
    private var locations : ArrayList<LatLng> = arrayListOf()
    private var beerNames : ArrayList<String> = arrayListOf()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?)
            : View? = inflater.inflate(
                    R.layout.view_map, container, false)


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
        loadLocations()
    }

    override fun onMapReady(map: GoogleMap?) {

        googleMap = map!!
        if (context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED
            || context?.let {
                ContextCompat.checkSelfPermission(
                    it,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                )
            } == PackageManager.PERMISSION_GRANTED) {
            googleMap.isMyLocationEnabled = true
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(context!!)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latLong = LatLng(location.latitude, location.longitude)
                    with(googleMap)
                    {
                        animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                latLong,
                                13f
                            )
                        )
                    }
                }
            }
        } else {
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                123
            )
        }

        with(googleMap) {

            val geocoder = android.location.Geocoder(
                activity!!.applicationContext,
                Locale.getDefault()
            )
            var title = ""
            var city = ""
            if(locations.isNotEmpty())
            {
                for(location in locations)
                {
                    try {
                        val addressList = geocoder.getFromLocation(
                            location.latitude,
                            location.longitude,
                            1
                        )
                        city = addressList[0].locality
                        title = addressList[0].getAddressLine(0)
                    } catch (e: Exception) {
                    }

                    val marker = addMarker(
                        MarkerOptions().position(
                            location
                        ).snippet(title).title(city)
                    )
                    marker.showInfoWindow()
                }
            }
        }
    }

    private fun loadLocations()
    {
        doAsync {
            val db = Room.databaseBuilder(activity!!.applicationContext,
                AppDatabase::class.java, "beers").build()
            beers = db.beerDao().getBeers()
            if(beers.isNotEmpty())
            {
                Log.v(TAG, "Yes entries")
            }
            else
            {
                Log.v(TAG, "No entries")
            }
            db.close()

            for (beer in beers)
            {
                val latLong = LatLng(beer.coord_lat, beer.coord_long)
                locations.add(latLong)
                beer.beer_name?.let { beerNames.add(it) }
                Log.v(TAG, "Inserted latitude: " + beer.coord_lat +
                                  " - longitude: " + beer.coord_long)
            }
        }
    }

    companion object {
        fun newInstance(): MapView = MapView()
    }
}

