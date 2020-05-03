package com.MobileComputing.BeerTracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jetbrains.anko.doAsync


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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val userInfo = UserInfo(
            uid = null,
            sex = -1,
            weight = 0.0
        )

            doAsync {
                val db = Room.databaseBuilder(applicationContext,
                    AppDatabase2::class.java, "user").build()
                if (!db.userDao().isUsed(1))
                {
                    db.userDao().insert(userInfo)
                    db.close()
                }
            }


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

    }

    override fun onBackPressed()
    {
    }
}


