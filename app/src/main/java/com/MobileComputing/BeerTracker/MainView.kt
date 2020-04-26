package com.MobileComputing.BeerTracker

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread

class MainView : Fragment() {


    private fun initSex(): Int? {
        var sex: Int? = null
        doAsync {
            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "user"
            ).build()
            sex = db.userDao().getSex()
            db.close()
        }
        return sex
    }

    private fun initWeight(): Double? {
        var weight: Double? = 0.0
        doAsync {
            val db = Room.databaseBuilder(
                activity!!.applicationContext,
                AppDatabase::class.java, "user"
            ).build()
            weight = db.userDao().getWeight()
            db.close()
        }
        return weight

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.view_main, container, false)
        var sex: Int? = initSex()
        var weight: Double? = initWeight()

        /* Check if weight and sex is added. Print text if not */
        if (sex == -1 || sex == null || weight == 0.0 || weight == null) {
            view.welcome.setText("Please input user info!")
            view.welcome.setTextColor(resources.getColor(R.color.error))
        } else {
            view.welcome.setText("You are durnk!")
            view.welcome.setTextColor(resources.getColor(R.color.allGood))
        }



        view.btn_addBeer.setOnClickListener {
            val intent = Intent(activity, AddBeerActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }

        // Return the fragment view/layout
        return view
    }

    companion object {
        fun newInstance(): MainView = MainView()
    }
}