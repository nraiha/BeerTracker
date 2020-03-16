package com.MobileComputing.BeerTracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var sel_frag: Fragment? = null

            when (item.itemId) {
                R.id.navigation_main -> sel_frag = MainView.newInstance()
                R.id.navigation_show_beer -> sel_frag =
                    ShowBeerView.newInstance()
                R.id.navigation_map -> sel_frag = MapView.newInstance()
                R.id.navigation_settings -> sel_frag =
                    SettingsView.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, sel_frag!!).commit()
            true
        }

/*
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
*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bottomNav = findViewById<BottomNavigationView>(
            R.id.bottom_navigation)
        bottomNav.setOnNavigationItemSelectedListener(navListener)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().replace(
                R.id.container, MainView.newInstance()).commit()

        }

        val mainView = MainView.newInstance()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, mainView)
        transaction.addToBackStack(null)
        transaction.commit()
    /*
        val bottomNavigation: BottomNavigationView =
                findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
        val mainView = MainView.newInstance()
        openFragment(mainView)

     */
    }
}


