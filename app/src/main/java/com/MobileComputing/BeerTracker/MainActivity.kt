package com.MobileComputing.BeerTracker

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private val navListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            var selectFragment: Fragment? = null

            when (item.itemId) {
                R.id.navigation_main -> selectFragment = MainView.newInstance()
                R.id.navigation_show_beer -> selectFragment =
                    ShowBeerView.newInstance()
                R.id.navigation_map -> selectFragment = MapView.newInstance()
                R.id.navigation_settings -> selectFragment =
                    SettingsView.newInstance()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, selectFragment!!).commit()
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
                return@OnNavigationItemSelectedListener trueo
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

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, mainView)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    /*
        val bottomNavigation: BottomNavigationView =
                findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(navListener)
        val mainView = MainView.newInstance()
        openFragment(mainView)

     */
    }

    // to let addToBackStack to be enabled
    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.container)
        (fragment as? IfOnBackButtonPressed)?.onBackPressed()?.not()?.let {
            super.onBackPressed()
        }
    }
}


