package com.MobileComputing.BeerTracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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

        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, mainView)

        // ideally this is done so that when user leaves fragment, the fragment is not deleted it is just paused.
        // and when the user returns to that fragment he/she continues from the same state it was left
        // but how to prevent that the user cannot return to earlier fragment when back button pressed but just enter the main view fragment
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

    override fun onBackPressed() {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.container, MainView.newInstance())
        fragmentTransaction.commit()
    }
}


