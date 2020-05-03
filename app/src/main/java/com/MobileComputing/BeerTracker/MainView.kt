package com.MobileComputing.BeerTracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.room.Room
import kotlinx.android.synthetic.main.view_main.view.*
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.uiThread
import java.time.Instant
import java.time.format.DateTimeFormatter
import java.util.*

class MainView : Fragment() {
    private fun getSex(): Int? {
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

    private fun getWeight(): Double? {
        var weight: Double? = null
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

    private fun getTimeLimit(): String
    {
        val cal: Calendar = Calendar.getInstance()
        cal.time = Date()
        cal.add(Calendar.HOUR, -12)
        return cal.time.toString()
    }

    private fun convertToSD(size: Double, percent: Double): Double
    {
        /* 0.789 is density of ethanol at room temperature */
        return size * percent * 0.789
    }

    private fun calculatePerMils(sex: Int?, Wt: Double?): Double
    {
        /*
         * Per mils are calculated with formula:
         *      (((0.806 * SD * 1.2) / (BW * Wt)) - MR * DP) * 10
         *
         * Where:
         *      SD = standard drinks.
         *          1 SD is one III beer. (3.3dl)
         *          IV beer is 1.4 SD (3.3dl)
         *
         *      SD is calculated:
         *          L * % * 0.789
         *          where:
         *              L = amount in litres
         *              % = alcohol percent
         *
         *      BW = body water constant (0.58 M / 0.49 F)
         *      Wt = body weight
         *      MR = metabolism constant (0.015 M / 0.017 F)
         *      DP = Drinking period hours
         */

        var SD: Double = 0.0
        var BW: Double = 0.0
        var Wt: Double = 0.0
        var MR: Double = 0.0
        var DP: Double = 0.0
        var perMils: Double = 0.0

        /* Male */
        when(sex) {
            0 -> { BW = 0.58; MR = 0.015 }
            1 -> { BW = 0.49; MR = 0.017 }
            /* We should never go here, what do? */
            else -> { return 100.0}
        }

        /* Use only beers from the last 12 hours */
        val timeLimit: String = getTimeLimit()

        /* Query the beers where timestamp is greater than the timeLimit */
        // SELECT BEERS HERE WHERE TIMPESTAMP >= TIMELIMIT

        /* Convert the beer size and alcohol percent to SD's*/
        //var percent = beer.percent
        //var size = beer.bottle_size
        //SD = convertToSD(size, percent)

        /* Calculate the per mils */
        perMils = (((0.806 * SD *1.2) / (BW * Wt)) - MR * DP) * 10
        if (perMils < 0) {
            return 0.0
        }
        return perMils
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view: View = inflater.inflate(R.layout.view_main, container, false)
        var sex: Int? = getSex()
        var weight: Double? = getWeight()

        var str: String = "\nSex: $sex\nWeight: $weight"
        Log.d("DEBUG", str)

        /* Check if weight and sex is added. Print text if not */
        if (sex == -1 || sex == null || weight == 0.0 || weight == null) {
            view.welcome.text = "Please input user info!"
            view.welcome.setTextColor(ContextCompat.getColor(
                context!!, R.color.error))
        } else {
            var x: Double = calculatePerMils(sex, weight)
            var str: String = "Your bloods alcoholic content is: $x\n"
            view.welcome.text = str
            view.welcome.setTextColor(ContextCompat.getColor(
                context!!,R.color.allGood))
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