package com.MobileComputing.BeerTracker

import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_add_beer.*

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class AddBeerActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var latLong: LatLng

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beer)

        btn_save.setOnClickListener {

            finish()
            val beerName = beerName.text.toString()
            if (beerName.isEmpty())
            {
                toast("Beer name cannot be empty")
                return@setOnClickListener
            }
            if (beerPerMills == null)
            {
                toast("Per mills cannot be empty")
            }

            try
            {
                val permilles = beerPerMills.text.toString().toFloat()
            }
            catch (e : NumberFormatException)
            {
                toast("Use . when inserting permills")
                // This needs to be fixed, currently returns to wrong page after exception
                return@setOnClickListener
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->

            }



            val beerItem = BeerItem(
                uid = null,
                beer_name = beerName,
                location = null,
                time = null,
                percentage = beerPerMills.text.toString().toFloat()
            )

            doAsync {
                val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "beers").build()
                val uid = db.beerDao().insert(beerItem).toInt()
                beerItem.uid = uid
                db.close()

            }
            toast("Entry added")
        }

        btn_cancel.setOnClickListener {
            finish()
        }
    }

}