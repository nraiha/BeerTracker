package com.MobileComputing.BeerTracker

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.nfc.Tag
import android.os.Bundle
import android.widget.Toast
import android.os.Looper
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.room.Room
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_add_beer.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast
import java.util.*


class AddBeerActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var lastLong : Double = 0.0
    private var lastLat : Double = 0.0

    private val TAG = "AddBeerActivity"
    private val REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beer)
        getLastLocation()
    }

    override fun onStart() {
        super.onStart()
        btn_save.setOnClickListener {
            finish()
            val beerName = beerName.text.toString()
            if (beerName.isEmpty()) {
                toast("Beer name cannot be empty")
                return@setOnClickListener
            }

            if (percentage == null)  {
                toast("Per mills cannot be empty")
                return@setOnClickListener
            }

            if (percentage.text.toString().toFloat() > 100) {
                toast("Percentage cannot be more than 100")
                return@setOnClickListener
            }

            if (size == null) {
                toast("Size cannot be empty")
                return@setOnClickListener
            }

            val cal: Calendar = Calendar.getInstance()
            cal.time = Date()

            val beerItem = BeerItem(
                uid = null,
                beer_name = beerName,
                coord_lat = lastLat,
                coord_long = lastLong,
                time = cal.time.time,
                percentage = percentage.text.toString().toFloat(),
                bottle_size = size.text.toString().toDouble()
            )

            doAsync {
                val db = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "beers").build()
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
    private fun getLastLocation() {
        if (checkPermissions()) {

            fusedLocationClient = LocationServices
                .getFusedLocationProviderClient(applicationContext)
            fusedLocationClient.lastLocation.addOnCompleteListener(this)

            {
                val location: Location? = it.result
                if(location != null) {
                    lastLat = location.latitude
                    lastLong = location.longitude
                }
            }
        } else {
            requestPermissions()
        }

    }

    private  fun requestPermissions() {
        val permissions = arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION)
        ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE)
    }

    private fun checkPermissions() : Boolean {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }

}
