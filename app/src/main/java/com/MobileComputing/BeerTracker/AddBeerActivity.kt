package com.MobileComputing.BeerTracker

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import kotlinx.android.synthetic.main.activity_add_beer.*
import kotlinx.android.synthetic.main.list_view_item.*
import java.time.LocalDateTime

import org.jetbrains.anko.doAsync
import org.jetbrains.anko.toast

class AddBeerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_beer)

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

            val beerItem = BeerItem(
                uid = null,
                beer_name = beerName,
                location = null,
                time = null,
                percentage = percentage.text.toString().toFloat()
            )

            doAsync {
                val db = Room.databaseBuilder(applicationContext,
                    AppDatabase::class.java, "beers").build()
                val uid = db.beerDao().insert(beerItem).toInt()
                beerItem.uid = uid
                db.close()
            }
        }

        btn_cancel.setOnClickListener {
            finish()
        }
    }

}