package com.MobileComputing.BeerTracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val m0nNavigationItemSelectedListener =
            BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_main -> {
                val mainView = MainView.newInstance()
                openFragment(mainView)

                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_show_beer -> {
                val showBeerView = ShowBeerView.newInstance()
                openFragment(showBeerView)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_map -> {
                val mapView = MapView.newInstance()
                openFragment(mapView)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {

                val settingsView = SettingsView.newInstance()
                openFragment(settingsView)
                return@OnNavigationItemSelectedListener true
            }
        }
                false
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNavigation: BottomNavigationView =
                findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(
                m0nNavigationItemSelectedListener)
        val mainView = MainView.newInstance()
        openFragment(mainView)
    }
}


